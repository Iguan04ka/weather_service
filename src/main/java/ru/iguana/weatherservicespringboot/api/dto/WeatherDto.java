package ru.iguana.weatherservicespringboot.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "DTO, содержащий погодные измерения")
public class WeatherDto {

    @Schema(description = "Время измерения", example = "2025-06-01T12:00:00")
    private LocalDateTime measuredAt;

    @Schema(description = "Список погодных характеристик по часам")
    private List<WeatherModel> weatherData;
}
