package ru.iguana.weatherservicespringboot.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WeatherServiceBeanConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    @Qualifier("YandexRestClient")
    public RestClient yandexRestClient(WeatherServiceProperties properties){
        return RestClient.builder().baseUrl(properties.getUrl().getYandex()).build();
    }

    @Bean
    @Qualifier("MeteoRestClient")
    public RestClient meteoRestClient(WeatherServiceProperties properties){
        return RestClient.builder().baseUrl(properties.getUrl().getMeteo()).build();
    }
}
