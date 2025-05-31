package ru.iguana.weatherservicespringboot.api.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WeatherDto {
    private LocalDateTime measuredAt;

    private List<WeatherModel> weatherData;

}
