package ru.iguana.weatherservicespringboot.api.service.impl;

import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.mapper.CityMapper;
import ru.iguana.weatherservicespringboot.api.mapper.WeatherMapper;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;
import ru.iguana.weatherservicespringboot.data.entity.WeatherEntity;
import ru.iguana.weatherservicespringboot.data.exception.CityNotFoundException;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;
import ru.iguana.weatherservicespringboot.data.repository.WeatherRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final RestClient restClientMeteo;

    private final RestClient restClientYandex;

    private final WeatherServiceProperties properties;

    private final WeatherRepository weatherRepository;

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    public WeatherServiceImpl(@Qualifier("MeteoRestClient") RestClient restClientMeteo,
                              @Qualifier("YandexRestClient") RestClient restClientYandex,
                              WeatherServiceProperties properties,
                              CityRepository cityRepository,
                              WeatherRepository weatherRepository,
                              CityMapper cityMapper) {
        this.restClientMeteo = restClientMeteo;
        this.restClientYandex= restClientYandex;
        this.properties= properties;
        this.weatherRepository = weatherRepository;
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }


    @Override
    public CityDto getForecastByCityName(String cityName) {
        try {
            Optional<CityEntity> cityEntity = cityRepository.findByName(cityName);

            if (cityEntity.isPresent()) {
                CityDto cityDto = cityMapper.toDto(cityEntity.get());
                return cityDto;
            }
            else throw new CityNotFoundException("No such city");
        }
        catch (RuntimeException e){
            throw e;
        }
    }
    @Override
    public void saveCityAndHisWeatherForecast(String cityName, String date){
        getAndSaveAllDataAboutWeatherAndCity(cityName, date);
    }
    @Override
    public void deleteCityByName(String name) {
        cityRepository.deleteCityEntityByName(name);
    }

    @Override
    @Transactional
    public void updateWeatherForecastByCityName(String cityName, String date) {
        CityEntity cityEntity = cityRepository.findByName(cityName)
                .orElseThrow(() -> new EntityNotFoundException("City not found with name: " + cityName));

        WeatherEntity weatherEntity = cityEntity.getWeather();
        if (weatherEntity == null) {
            weatherEntity = new WeatherEntity();
            cityEntity.setWeather(weatherEntity);
        }

        List<WeatherModel> weatherData = fetchMeteoApi(cityName, date);

        weatherEntity.setWeatherData(weatherData);
        weatherEntity.setMeasuredAt(LocalDateTime.now());

        weatherRepository.save(weatherEntity);

        cityRepository.save(cityEntity);
    }
    private void getAndSaveAllDataAboutWeatherAndCity(String cityName, String date) {
        Coordinates coord = getCoordinates(cityName);

        List<WeatherModel> weatherData = fetchMeteoApi(cityName, date);

        WeatherEntity weatherEntity = saveWeatherEntity(weatherData);

        saveCityEntity(weatherEntity, coord.lat, coord.lon, cityName);
    }

    private Coordinates getCoordinates(String cityName) {
        String jsonResponse = fetchYandexApi(cityName);

        String pos = JsonPath.read(jsonResponse, "$.response.GeoObjectCollection.featureMember[0].GeoObject.Point.pos");
        String[] parts = pos.split(" "); //поле имеет вид "pos":"lat lon", поэтому разделяем по пробелу

        return new Coordinates(
                Float.parseFloat(parts[0]),
                Float.parseFloat(parts[1])
        );
    }

    private String fetchYandexApi(String cityName){
        return restClientYandex.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", properties.getToken().getYandex())
                        .queryParam("geocode", cityName)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(String.class);
    }

    private List<WeatherModel> fetchMeteoApi(String cityName, String date){
        Coordinates coord = getCoordinates(cityName);

        WeatherModel[] response = restClientMeteo.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", coord.lat)
                        .queryParam("lon", coord.lon)
                        .queryParam("date", date)
                        .queryParam("token", properties.getToken().getMeteo())
                        .build())
                .retrieve()
                .body(WeatherModel[].class);

        List<WeatherModel> weatherData = Arrays.asList(response);

        return weatherData;
    }

    private WeatherEntity saveWeatherEntity(List<WeatherModel> weatherData){
        WeatherEntity weatherEntity = new WeatherEntity()
                .setWeatherData(weatherData)
                .setMeasuredAt(LocalDateTime.now());
        weatherRepository.save(weatherEntity);
        return weatherEntity;
    }
    private CityEntity saveCityEntity(WeatherEntity weather, Float lat, Float lon, String cityName){
        CityEntity cityEntity = new CityEntity()
                .setName(cityName)
                .setLat(lat)
                .setLon(lon)
                .setWeather(weather);
        cityRepository.save(cityEntity);
        return cityEntity;
    }

    @Data
    @AllArgsConstructor
    private static class Coordinates{
        Float lat;
        Float lon;
    }
}

