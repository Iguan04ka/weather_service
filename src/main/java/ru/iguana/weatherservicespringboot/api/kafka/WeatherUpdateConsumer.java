package ru.iguana.weatherservicespringboot.api.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherUpdateConsumer {
    private final WeatherService weatherService;

    @KafkaListener(topics = "${kafka.topic.city-update}", groupId = "weather-group")
    public void consumeCityNames(List<String> cityNames) {
        log.info("Received city list from Kafka: {}", cityNames);

        cityNames.forEach(cityName -> {
            try {
                String today = LocalDate.now().toString();
                weatherService.updateWeatherForecastByCityName(cityName, today);
                log.info("Updated weather for city from Kafka: {}", cityName);
            } catch (Exception e) {
                log.error("Failed to update weather for city: {}", cityName, e);
            }
        });
    }
}

