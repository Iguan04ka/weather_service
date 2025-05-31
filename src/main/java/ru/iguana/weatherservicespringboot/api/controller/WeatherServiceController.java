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


//    @GetMapping("/weather/findAll")
//    public ResponseEntity<List<CityDto>> getAllCities(){
//        log.info("Received request to get all cities and they weather");
//        try{
//            List<CityDto> response = weatherService.findAllCities();
//            log.info("All cities successfully retrieved");
//            return ResponseEntity.ok(response);
//        }
//        catch (Exception e){
//            log.error("Error request to get all cities and they weather");
//            throw e;
//        }
//    }
//
//    @GetMapping("/weather/find/{name}")
//    public ResponseEntity<CityDto> getCity(@PathVariable("name") String name){
//        log.info("Received request to get city " + name + " and his weather");
//        try {
//            CityDto response = weatherService.findCityByName(name);
//            log.info("City with name " + name + " successfully retrieved");
//            return ResponseEntity.ok(response);
//        }
//        catch (Exception e){
//            log.error("Error request to get city " + name + " and his weather");
//            throw e;
//        }
//    }
//
//    @PostMapping("/weather/save/{name}")
//    public ResponseEntity<Void> saveCity(@PathVariable("name") String name){
//        log.info("Received request to save city " + name);
//        try {
//            weatherService.saveCity(name);
//            log.info("City with name " + name + " successfully saved");
//            return ResponseEntity.ok().build();
//        }
//        catch (Exception e){
//            log.error("Error request to save city " + name);
//            throw e;
//        }
//    }
//
//    @DeleteMapping("/weather/delete/{name}")
//    public ResponseEntity<Void> deleteCity(@PathVariable("name") String name){
//        log.info("Received request to delete city " + name);
//        try{
//            weatherService.deleteCity(name);
//            log.info("City with name " + name + " successfully deleted");
//            return ResponseEntity.ok().build();
//        }
//        catch (Exception e){
//            log.error("Error request to delete city " + name);
//            throw e;
//        }
//    }
    @GetMapping("/weather/getForecast")
    public ResponseEntity<CityDto> getForecast(@RequestParam String cityName){
        return ResponseEntity.ok(weatherService.getForecastByCityName(cityName));
    }
    @PostMapping("/weather/saveForecast")
    public ResponseEntity<Void> saveForecast(@RequestParam String cityName,
                                             @RequestParam String date){
        weatherService.saveCityAndHisWeatherForecast(cityName, date);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/weather/deleteCity")
    public ResponseEntity<Void> deleteCity(@RequestParam String cityName){
        weatherService.deleteCityByName(cityName);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/weather/updateForecast")
    public ResponseEntity<Void> updateForecast(@RequestParam String cityName,
                                               @RequestParam String date){
        weatherService.updateWeatherForecastByCityName(cityName, date);
        return ResponseEntity.ok().build();
    }
}
