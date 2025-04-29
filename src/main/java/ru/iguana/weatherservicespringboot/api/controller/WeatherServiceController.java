package ru.iguana.weatherservicespringboot.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WeatherServiceController {
    private final WeatherService weatherService;

    @GetMapping("/weather/findAll")
    public ResponseEntity<List<CityDto>> getAllCities(){
        return ResponseEntity.ok(weatherService.findAllCities());
    }

    @GetMapping("/weather/find/{name}")
    public ResponseEntity<CityDto> getCity(@PathVariable("name") String name){
        return ResponseEntity.ok(weatherService.findCityByName(name));
    }

    @PostMapping("/weather/save/{name}")
    public ResponseEntity<Void> saveCity(@PathVariable("name") String name){
        weatherService.saveCity(name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/weather/delete/{name}")
    public ResponseEntity<Void> deleteCity(@PathVariable("name") String name){
        weatherService.deleteCity(name);
        return ResponseEntity.ok().build();
    }


}
