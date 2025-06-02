package ru.iguana.weatherservicespringboot.api.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityUpdateScheduler {

    private final CityRepository cityRepository;
    private final KafkaTemplate<String, List<String>> kafkaTemplate;

    @Value("${kafka.topic.city-update}")
    private String topic;

    @Scheduled(fixedRate = 21600000) // 6 часов в миллисекундах = 21600000
    public void sendCityNamesToKafka() {
        List<CityEntity> cities = cityRepository.findAll();
        List<String> cityNames = cities.stream()
                .map(CityEntity::getName)
                .toList();
        kafkaTemplate.send(topic, cityNames);
        log.info("Sent city list to Kafka: {}", cityNames);
    }
}
