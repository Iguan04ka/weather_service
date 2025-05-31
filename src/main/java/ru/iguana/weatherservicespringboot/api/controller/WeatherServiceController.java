package ru.iguana.weatherservicespringboot.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceController {
    private final WeatherService weatherService;

    @GetMapping("/weather/getForecast")
    public ResponseEntity<CityDto> getForecast(@RequestParam String cityName) {
        log.info("GET /weather/getForecast request received for city: {}", cityName);
        try {
            CityDto response = weatherService.getForecastByCityName(cityName);
            log.info("GET /weather/getForecast successful for city: {}, response: {}", cityName, response);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error in GET /weather/getForecast for city: {}", cityName, e);
            throw e;
        }
    }

    @PostMapping("/weather/saveForecast")
    public ResponseEntity<Void> saveForecast(@RequestParam String cityName,
                                             @RequestParam String date) {
        log.info("POST /weather/saveForecast request received for city: {}, date: {}", cityName, date);
        try {
            weatherService.saveCityAndHisWeatherForecast(cityName, date);
            log.info("POST /weather/saveForecast successful for city: {}, date: {}", cityName, date);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error in POST /weather/saveForecast for city: {}, date: {}", cityName, date, e);
            throw e;
        }
    }

    @DeleteMapping("/weather/deleteCity")
    public ResponseEntity<Void> deleteCity(@RequestParam String cityName) {
        log.info("DELETE /weather/deleteCity request received for city: {}", cityName);
        try {
            weatherService.deleteCityByName(cityName);
            log.info("DELETE /weather/deleteCity successful for city: {}", cityName);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error in DELETE /weather/deleteCity for city: {}", cityName, e);
            throw e;
        }
    }
    @PatchMapping("/weather/updateForecast")
    public ResponseEntity<Void> updateForecast(@RequestParam String cityName,
                                               @RequestParam String date) {
        log.info("PATCH /weather/updateForecast request received for city: {}, date: {}", cityName, date);
        try {
            weatherService.updateWeatherForecastByCityName(cityName, date);
            log.info("PATCH /weather/updateForecast successful for city: {}, date: {}", cityName, date);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error in PATCH /weather/updateForecast for city: {}, date: {}", cityName, date, e);
            throw e;
        }
    }
}
