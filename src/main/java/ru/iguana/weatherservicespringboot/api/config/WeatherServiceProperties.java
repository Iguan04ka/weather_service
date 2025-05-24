package ru.iguana.weatherservicespringboot.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class WeatherServiceProperties {
    private Token token;
    private Url url;

    @Getter
    @Setter
    public static class Token {
        private String yandex;
        private String meteo;
    }

    @Getter
    @Setter
    public static class Url {
        private String yandex;
        private String meteo;
    }
}
