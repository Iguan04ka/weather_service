package ru.iguana.weatherservicespringboot.api.service.impl;

import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.mapper.CityMapper;
import ru.iguana.weatherservicespringboot.api.service.CityWeatherBuilderService;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;
import ru.iguana.weatherservicespringboot.data.entity.WeatherEntity;
import ru.iguana.weatherservicespringboot.data.exception.CityNotFoundException;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;
import ru.iguana.weatherservicespringboot.data.repository.WeatherRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final CityWeatherBuilderService cityWeatherBuilderService;

    private final WeatherRepository weatherRepository;

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    private final WeatherCacheService weatherCacheService;

    public WeatherServiceImpl(CityWeatherBuilderService cityWeatherBuilderService,
                              CityRepository cityRepository,
                              WeatherRepository weatherRepository,
                              CityMapper cityMapper,
                              WeatherCacheService weatherCacheService) {

        this.weatherRepository = weatherRepository;
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.weatherCacheService = weatherCacheService;
        this.cityWeatherBuilderService = cityWeatherBuilderService;
    }

    @Override
    public CityDto getForecastByCityName(String cityName) {
        log.info("Getting forecast for city: {}", cityName);
        try {
            CityDto cachedData = weatherCacheService.get(cityName);
            if (cachedData != null) {
                log.info("Returning cached data for city: {}", cityName);
                return cachedData;
            }

            Optional<CityEntity> cityEntity = cityRepository.findByName(cityName);

            if (cityEntity.isPresent()) {
                CityDto cityDto = cityMapper.toDto(cityEntity.get());

                weatherCacheService.put(cityDto.getName(), cityDto);
                log.info("Successfully retrieved and cached forecast for city: {}, data: {}", cityName, cityDto);
                return cityDto;
            } else {
                log.warn("City not found: {}", cityName);
                throw new CityNotFoundException("No such city");
            }
        } catch (RuntimeException e) {
            log.error("Error getting forecast for city: {}", cityName, e);
            throw e;
        }
    }


    @Override
    public void saveCityAndHisWeatherForecast(String cityName, String date) {
        log.info("Saving city and weather forecast for city: {}, date: {}", cityName, date);
        try {
            CityEntity cityEntity = cityWeatherBuilderService.buildAndSaveCityWithWeather(cityName, date);
            CityDto cityDto = cityMapper.toDto(cityEntity);

            weatherCacheService.put(cityDto.getName(), cityDto);

            log.info("Successfully saved city and weather for city: {}, date: {}. Data cached.", cityName, date);
        } catch (RuntimeException e) {
            log.error("Error saving city and weather for city: {}, date: {}", cityName, date, e);
            throw e;
        }
    }

    @Override
    public void deleteCityByName(String name) {
        log.info("Deleting city by name: {}", name);
        try {
            cityRepository.deleteCityEntityByName(name);
            log.info("Successfully deleted city: {}", name);
        } catch (RuntimeException e) {
            log.error("Error deleting city: {}", name, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void updateWeatherForecastByCityName(String cityName, String date) {
        log.info("Updating weather forecast for city: {}, date: {}", cityName, date);
        try {
            CityEntity cityEntity = cityRepository.findByName(cityName)
                    .orElseThrow(() -> {
                        log.warn("City not found: {}", cityName);
                        return new EntityNotFoundException("City not found with name: " + cityName);
                    });

            WeatherEntity weatherEntity = cityEntity.getWeather();
            if (weatherEntity == null) {
                log.debug("Creating new weather entity for city: {}", cityName);
                weatherEntity = new WeatherEntity();
                cityEntity.setWeather(weatherEntity);
            }

            List<WeatherModel> weatherData = cityWeatherBuilderService.fetchMeteoApi(cityName, date);
            log.debug("Fetched weather data for update: {}", weatherData);

            weatherEntity.setWeatherData(weatherData);
            weatherEntity.setMeasuredAt(LocalDateTime.now());

            weatherRepository.save(weatherEntity);
            cityRepository.save(cityEntity);

            CityDto cityDto = cityMapper.toDto(cityEntity);

            weatherCacheService.put(cityName, cityDto);

            log.info("Successfully cached weather after update for city: {}", cityName);

            log.info("Successfully updated weather for city: {}", cityName);
        } catch (RuntimeException e) {
            log.error("Error updating weather for city: {}", cityName, e);
            throw e;
        }
    }
}

