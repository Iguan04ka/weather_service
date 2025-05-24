package ru.iguana.weatherservicespringboot.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iguana.weatherservicespringboot.api.config.WeatherServiceProperties;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.dto.WeatherApiDto;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;
import ru.iguana.weatherservicespringboot.api.service.impl.WeatherServiceREST;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceController {
    private final WeatherService weatherService;

    private final WeatherServiceREST serviceREST;

    @GetMapping("/weather/findAll")
    public ResponseEntity<List<CityDto>> getAllCities(){
        log.info("Received request to get all cities and they weather");
        try{
            List<CityDto> response = weatherService.findAllCities();
            log.info("All cities successfully retrieved");
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            log.error("Error request to get all cities and they weather");
            throw e;
        }
    }

    @GetMapping("/weather/find/{name}")
    public ResponseEntity<CityDto> getCity(@PathVariable("name") String name){
        log.info("Received request to get city " + name + " and his weather");
        try {
            CityDto response = weatherService.findCityByName(name);
            log.info("City with name " + name + " successfully retrieved");
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            log.error("Error request to get city " + name + " and his weather");
            throw e;
        }
    }

    @PostMapping("/weather/save/{name}")
    public ResponseEntity<Void> saveCity(@PathVariable("name") String name){
        log.info("Received request to save city " + name);
        try {
            weatherService.saveCity(name);
            log.info("City with name " + name + " successfully saved");
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            log.error("Error request to save city " + name);
            throw e;
        }
    }

    @DeleteMapping("/weather/delete/{name}")
    public ResponseEntity<Void> deleteCity(@PathVariable("name") String name){
        log.info("Received request to delete city " + name);
        try{
            weatherService.deleteCity(name);
            log.info("City with name " + name + " successfully deleted");
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            log.error("Error request to delete city " + name);
            throw e;
        }
    }
    @GetMapping("/weather/qwe")
    public ResponseEntity<List<WeatherApiDto>> qwe(@RequestParam String address,
                                                   @RequestParam String date){
        return ResponseEntity.ok(serviceREST.getWeatherData(address, date));
    }
}
