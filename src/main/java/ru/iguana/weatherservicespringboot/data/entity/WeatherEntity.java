package ru.iguana.weatherservicespringboot.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;


@Entity
@Table(schema = "public", name = "weather")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer temperature;

    @Column(nullable = false)
    private Integer humidity;

    @Column(name = "wind_speed", nullable = false)
    private Integer windSpeed;

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    public static WeatherEntity createRandomWeather() {
        Random random = new Random();
        WeatherEntity weather = new WeatherEntity();
        weather.setTemperature(random.nextInt(60) - 20);
        weather.setHumidity(random.nextInt(101));
        weather.setWindSpeed(random.nextInt(31));
        weather.setMeasuredAt(LocalDateTime.now());
        return weather;
    }

}
