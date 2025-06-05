package ru.iguana.weatherservicespringboot.data.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель данных прогноза погоды")
public class WeatherModel {

    @JsonAlias("dt_forecast")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Дата и время прогноза", example = "2025-06-01T12:00:00")
    private LocalDateTime forecastDate;

    @JsonAlias("temp_100_cel")
    @Schema(description = "Температура в градусах Цельсия", example = "21.5")
    private Float temperature;

    @JsonAlias("temp_feels_cel")
    @Schema(description = "Ощущаемая температура в градусах Цельсия", example = "20.0")
    private Float feelingTemperature;

    @JsonAlias("wind_speed_10")
    @Schema(description = "Скорость ветра в м/с", example = "5.4")
    private Float windSpeed;

    @JsonAlias("vlaga_2")
    @Schema(description = "Влажность в процентах", example = "65.0")
    private Float humidity;
}
