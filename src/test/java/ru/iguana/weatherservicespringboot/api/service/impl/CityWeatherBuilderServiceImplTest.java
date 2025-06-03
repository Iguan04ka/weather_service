package ru.iguana.weatherservicespringboot.api.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;
import ru.iguana.weatherservicespringboot.data.entity.WeatherEntity;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;
import ru.iguana.weatherservicespringboot.data.repository.WeatherRepository;

import java.time.LocalDateTime;
import java.util.function.Function;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityWeatherBuilderServiceImplTest {

    @Mock
    private RestClient restClientMeteo;

    @Mock
    private RestClient restClientYandex;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private WeatherServiceProperties properties;

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityWeatherBuilderServiceImpl builderService;

    @Test
    void buildAndSaveCityWithWeather_shouldBuildAndSaveCity() {
        // GIVEN
        String cityName = "Москва";
        String date = "2025-01-01";

        String yandexResponse = """
            {
              "response": {
                "GeoObjectCollection": {
                  "featureMember": [
                    {
                      "GeoObject": {
                        "Point": {
                          "pos": "37.6173 55.7558"
                        }
                      }
                    }
                  ]
                }
              }
            }
            """;

        Float lat = 37.6173f;
        Float lon = 55.7558f;

        WeatherModel weatherModel = new WeatherModel();
        WeatherModel[] weatherResponseArray = new WeatherModel[]{weatherModel};
        List<WeatherModel> weatherData = List.of(weatherModel);
        WeatherServiceProperties.Url url = new WeatherServiceProperties.Url();
        url.setYandex("https://geocode-maps.yandex.ru/v1/");
        url.setMeteo("https://projecteol.ru/api/weather/");

        WeatherEntity savedWeather = new WeatherEntity()
                .setWeatherData(weatherData);
        CityEntity savedCity = new CityEntity()
                .setName(cityName)
                .setLat(lat)
                .setLon(lon)
                .setWeather(savedWeather);

        // MOCK: Yandex REST Client
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(eq(String.class))).thenReturn(yandexResponse);

        // MOCK: Meteo REST Client
        when(restClientMeteo.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(eq(WeatherModel[].class))).thenReturn(weatherResponseArray);

        // MOCK: Repository saves
        when(weatherRepository.save(any())).thenReturn(savedWeather);
        when(cityRepository.save(any())).thenReturn(savedCity);

        // WHEN
        CityEntity result = builderService.buildAndSaveCityWithWeather(cityName, date);


        // THEN
        assertNotNull(result);
        assertEquals(cityName, result.getName());
        assertEquals(lat, result.getLat());
        assertEquals(lon, result.getLon());
        assertNotNull(result.getWeather().getMeasuredAt());
        assertEquals(savedWeather.getWeatherData(), result.getWeather().getWeatherData());
    }
}


