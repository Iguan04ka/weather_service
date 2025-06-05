package ru.iguana.weatherservicespringboot.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iguana.weatherservicespringboot.data.annotation.RussianLettersOnly;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;

@RestController
@Slf4j
@Tag(name = "Weather API", description = "Контроллер для получения и управления прогнозом погоды по городам")
public abstract class AbstractWeatherServiceController {
    @Operation(
            summary = "Получить прогноз погоды",
            description = "Возвращает прогноз погоды по заданному названию города",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный ответ"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public ResponseEntity<CityDto> getForecast(
            @RequestParam
            @RussianLettersOnly
            @NotNull @NotBlank
            @Parameter(description = "Название города (только русские буквы)", example = "Москва")
            String cityName) {
        throw new UnsupportedOperationException("This method should be implemented by subclass");
    }

    @Operation(
            summary = "Сохранить прогноз погоды",
            description = "Сохраняет прогноз погоды по названию города и дате",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Прогноз сохранён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный ввод"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
            }
    )
    public ResponseEntity<Void> saveForecast(
            @RequestParam
            @RussianLettersOnly
            @NotNull @NotBlank
            @Parameter(description = "Название города", example = "Санкт-Петербург")
            String cityName,

            @RequestParam
            @NotNull @NotBlank
            @Parameter(description = "Дата прогноза в формате yyyy-MM-dd", example = "2025-06-02")
            String date) {
        throw new UnsupportedOperationException("This method should be implemented by subclass");
    }

    @Operation(
            summary = "Удалить город",
            description = "Удаляет сохранённый прогноз погоды для указанного города",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Удалено успешно"),
                    @ApiResponse(responseCode = "404", description = "Город не найден"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
            }
    )
    public ResponseEntity<Void> deleteCity(
            @RequestParam
            @RussianLettersOnly
            @NotNull @NotBlank
            @Parameter(description = "Название города", example = "Казань")
            String cityName) {
        throw new UnsupportedOperationException("This method should be implemented by subclass");
    }

    @Operation(
            summary = "Обновить прогноз",
            description = "Обновляет прогноз погоды по городу и дате",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Прогноз обновлён"),
                    @ApiResponse(responseCode = "400", description = "Некорректный ввод"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
            }
    )
    public ResponseEntity<Void> updateForecast(
            @RequestParam
            @RussianLettersOnly
            @NotNull @NotBlank
            @Parameter(description = "Название города", example = "Новосибирск")
            String cityName,

            @RequestParam
            @NotNull @NotBlank
            @Parameter(description = "Дата прогноза", example = "2025-06-05")
            String date) {
        throw new UnsupportedOperationException("This method should be implemented by subclass");
    }
}
