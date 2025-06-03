package ru.iguana.weatherservicespringboot.api.service.impl;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;
import ru.iguana.weatherservicespringboot.api.service.CityWeatherBuilderService;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;
import ru.iguana.weatherservicespringboot.data.entity.WeatherEntity;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;
import ru.iguana.weatherservicespringboot.data.repository.WeatherRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class CityWeatherBuilderServiceImpl implements CityWeatherBuilderService {

    private final RestClient restClientMeteo;
    private final RestClient restClientYandex;
    private final WeatherServiceProperties properties;
    private final WeatherRepository weatherRepository;
    private final CityRepository cityRepository;

    public CityWeatherBuilderServiceImpl(@Qualifier("MeteoRestClient") RestClient restClientMeteo,
                                         @Qualifier("YandexRestClient") RestClient restClientYandex,
                                         WeatherServiceProperties properties,
                                         WeatherRepository weatherRepository,
                                         CityRepository cityRepository) {
        this.restClientMeteo = restClientMeteo;
        this.restClientYandex = restClientYandex;
        this.properties = properties;
        this.weatherRepository = weatherRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public CityEntity buildAndSaveCityWithWeather(String cityName, String date) {
        log.debug("Getting and saving all data for city: {}, date: {}", cityName, date);

        Coordinates coord = getCoordinates(cityName);
        List<WeatherModel> weatherData = fetchMeteoApi(cityName, date);

        WeatherEntity weatherEntity = saveWeatherEntity(weatherData);
        return saveCityEntity(weatherEntity, coord.lat, coord.lon, cityName);
    }

    @Override
    public List<WeatherModel> fetchMeteoApi(String cityName, String date) {
        log.debug("Fetching Meteo API for city: {}, date: {}", cityName, date);
        try {
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
            log.debug("Retrieved weather data: {}", weatherData);
            return weatherData;
        } catch (RuntimeException e) {
            log.error("Error fetching Meteo API for city: {}", cityName, e);
            throw e;
        }
    }

    private CityEntity getAndSaveAllDataAboutWeatherAndCity(String cityName, String date) {
        log.debug("Getting and saving all data for city: {}, date: {}", cityName, date);
        try {
            Coordinates coord = getCoordinates(cityName);
            log.debug("Retrieved coordinates: lat={}, lon={}", coord.lat, coord.lon);

            List<WeatherModel> weatherData = fetchMeteoApi(cityName, date);
            log.debug("Fetched weather data: {}", weatherData);

            WeatherEntity weatherEntity = saveWeatherEntity(weatherData);
            log.debug("Saved weather entity: {}", weatherEntity.getId());

            CityEntity cityEntity = saveCityEntity(weatherEntity, coord.lat, coord.lon, cityName);
            log.debug("Saved city entity: {}", cityEntity.getId());

            return cityEntity;

        } catch (RuntimeException e) {
            log.error("Error in getAndSaveAllDataAboutWeatherAndCity for city: {}", cityName, e);
            throw e;
        }
    }
    private Coordinates getCoordinates(String cityName) {
        log.debug("Getting coordinates for city: {}", cityName);
        try {
            String jsonResponse = fetchYandexApi(cityName);
            String pos = JsonPath.read(jsonResponse, "$.response.GeoObjectCollection.featureMember[0].GeoObject.Point.pos");
            String[] parts = pos.split(" ");

            Coordinates coordinates = new Coordinates(
                    Float.parseFloat(parts[0]),
                    Float.parseFloat(parts[1])
            );
            log.debug("Parsed coordinates: {}", coordinates);
            return coordinates;
        } catch (RuntimeException e) {
            log.error("Error getting coordinates for city: {}", cityName, e);
            throw e;
        }
    }
    public String fetchYandexApi(String cityName) {
        log.debug("Fetching Yandex API for city: {}", cityName);
        try {
            return restClientYandex.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("apikey", properties.getToken().getYandex())
                            .queryParam("geocode", cityName)
                            .queryParam("format", "json")
                            .build())
                    .retrieve()
                    .body(String.class);
        } catch (RuntimeException e) {
            log.error("Error fetching Yandex API for city: {}", cityName, e);
            throw e;
        }
    }



    private WeatherEntity saveWeatherEntity(List<WeatherModel> weatherData) {
        log.debug("Saving weather entity with data: {}", weatherData);
        try {
            WeatherEntity weatherEntity = new WeatherEntity()
                    .setWeatherData(weatherData)
                    .setMeasuredAt(LocalDateTime.now());
            weatherRepository.save(weatherEntity);
            log.debug("Saved weather entity with ID: {}", weatherEntity.getId());
            return weatherEntity;
        } catch (RuntimeException e) {
            log.error("Error saving weather entity", e);
            throw e;
        }
    }

    private CityEntity saveCityEntity(WeatherEntity weather, Float lat, Float lon, String cityName) {
        log.debug("Saving city entity: {}, lat: {}, lon: {}", cityName, lat, lon);
        try {
            CityEntity cityEntity = new CityEntity()
                    .setName(cityName)
                    .setLat(lat)
                    .setLon(lon)
                    .setWeather(weather);
            cityRepository.save(cityEntity);
            log.debug("Saved city entity with ID: {}", cityEntity.getId());
            return cityEntity;
        } catch (RuntimeException e) {
            log.error("Error saving city entity: {}", cityName, e);
            throw e;
        }
    }

    @Data
    @AllArgsConstructor
    private static class Coordinates {
        Float lat;
        Float lon;
    }
}
