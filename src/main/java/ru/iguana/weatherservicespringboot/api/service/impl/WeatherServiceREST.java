package ru.iguana.weatherservicespringboot.api.service.impl;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;
import ru.iguana.weatherservicespringboot.api.dto.WeatherApiDto;

import java.util.Arrays;
import java.util.List;

@Service
public class WeatherServiceREST {
    private final RestClient restClientMeteo;
    private final RestClient restClientYandex;
    private final WeatherServiceProperties properties;

    public WeatherServiceREST(@Qualifier("MeteoRestClient") RestClient restClientMeteo,
                              @Qualifier("YandexRestClient") RestClient restClientYandex,
                              WeatherServiceProperties properties) {
        this.restClientMeteo = restClientMeteo;
        this.restClientYandex= restClientYandex;
        this.properties= properties;
    }

    public List<WeatherApiDto> getWeatherData(String cityName, String date) {

        WeatherApiDto[] response = fetchMeteoApi(cityName, date);

        return Arrays.asList(response);
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

    private WeatherApiDto[] fetchMeteoApi(String cityName, String date){
        Coordinates coord = getCoordinates(cityName);

        return restClientMeteo.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", coord.lat)
                        .queryParam("lon", coord.lon)
                        .queryParam("date", date)
                        .queryParam("token", properties.getToken().getMeteo())
                        .build())
                .retrieve()
                .body(WeatherApiDto[].class);
    }
    @Data
    @AllArgsConstructor
    private static class Coordinates{
        Float lat;
        Float lon;
    }
}

