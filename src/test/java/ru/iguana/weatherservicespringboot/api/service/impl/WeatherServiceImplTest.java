package ru.iguana.weatherservicespringboot.api.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.mapper.CityMapper;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;
import ru.iguana.weatherservicespringboot.data.entity.WeatherEntity;
import ru.iguana.weatherservicespringboot.data.exception.CityNotFoundException;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;
import ru.iguana.weatherservicespringboot.data.repository.WeatherRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock
    private CityWeatherBuilderServiceImpl cityWeatherBuilderService;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private CityMapper cityMapper;

    @Mock
    private WeatherCacheService weatherCacheService;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    void getForecastByCityName_shouldReturnCachedData() {
        String cityName = "Москва";
        CityDto cached = new CityDto();
        Mockito.when(weatherCacheService.get(cityName)).thenReturn(cached);

        CityDto result = weatherService.getForecastByCityName(cityName);

        assertEquals(cached, result);
        Mockito.verify(cityRepository, Mockito.never()).findByName(any());
    }

    @Test
    void getForecastByCityName_shouldLoadFromDbIfNotCached() {
        String cityName = "Саратов";
        CityEntity cityEntity = new CityEntity();
        CityDto dto = new CityDto();
        dto.setName(cityName);

        Mockito.when(weatherCacheService.get(cityName)).thenReturn(null);
        Mockito.when(cityRepository.findByName(cityName)).thenReturn(Optional.of(cityEntity));
        Mockito.when(cityMapper.toDto(cityEntity)).thenReturn(dto);

        CityDto result = weatherService.getForecastByCityName(cityName);

        assertEquals(dto, result);
        Mockito.verify(weatherCacheService).put(cityName, dto);
    }

    @Test
    void getForecastByCityName_shouldThrowIfCityNotFound() {
        String cityName = "Unknown";
        Mockito.when(weatherCacheService.get(cityName)).thenReturn(null);
        Mockito.when(cityRepository.findByName(cityName)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () ->
                weatherService.getForecastByCityName(cityName));
    }

    @Test
    void saveCityAndHisWeatherForecast_shouldSaveAndCache() {
        String city = "Москва";
        String date = "2025-01-01";

        CityEntity entity = new CityEntity();
        CityDto dto = new CityDto();
        dto.setName(city);


        Mockito.doReturn(entity).when(cityWeatherBuilderService).buildAndSaveCityWithWeather(city, date);
        Mockito.when(cityMapper.toDto(entity)).thenReturn(dto);

        weatherService.saveCityAndHisWeatherForecast(city, date);

        Mockito.verify(weatherCacheService).put(city, dto);
    }

    @Test
    void deleteCityByName_shouldCallRepository() {
        String city = "Москва";

        weatherService.deleteCityByName(city);

        Mockito.verify(cityRepository).deleteCityEntityByName(city);
    }

    @Test
    void updateWeatherForecastByCityName_shouldUpdateEntities() {
        String city = "Москва";
        String date = "2025-01-01";

        CityEntity cityEntity = new CityEntity().setName(city);
        WeatherEntity weather = new WeatherEntity();
        List<WeatherModel> data = List.of(new WeatherModel());
        CityDto dto = new CityDto();

        cityEntity.setWeather(weather);

        Mockito.when(cityRepository.findByName(city)).thenReturn(Optional.of(cityEntity));
        Mockito.doReturn(data).when(cityWeatherBuilderService).fetchMeteoApi(city, date);
        Mockito.when(cityMapper.toDto(cityEntity)).thenReturn(dto);

        weatherService.updateWeatherForecastByCityName(city, date);

        Mockito.verify(weatherRepository).save(weather);
        Mockito.verify(cityRepository).save(cityEntity);
        Mockito.verify(weatherCacheService).put(city, dto);
    }

    @Test
    void updateWeatherForecastByCityName_shouldThrowIfCityNotFound() {
        String city = "Москва";
        Mockito.when(cityRepository.findByName(city)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                weatherService.updateWeatherForecastByCityName(city, "2025-01-01"));
    }
}
