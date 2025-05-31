package ru.iguana.weatherservicespringboot.data.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Entity
@Table(schema = "public", name = "weather")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "measured_at")
    private LocalDateTime measuredAt;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", name = "weather_data")
    private List<WeatherModel> weatherData = new ArrayList<>();

}
