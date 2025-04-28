package ru.iguana.weatherservicespringboot.api.controller;

import org.springframework.web.bind.annotation.*;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;
import ru.iguana.weatherservicespringboot.data.model.City;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;

import java.util.Collection;

@RestController
public class WeatherServiceController {
    private final WeatherService weatherService;

    public WeatherServiceController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/findAll")
    public Collection<City> getAllCities(){
        return weatherService.findAll();
    }

    @GetMapping("/weather/find/{name}")
    public City getCity(@PathVariable String name){
        return weatherService.findOneByName(name);
    }

    @PostMapping("/weather/save/{name}")
    public void saveCity(@PathVariable String name){
        weatherService.create(name);
    }

    @DeleteMapping("/weather/delete/{name}")
    public void deleteCity(@PathVariable String name){
        weatherService.delete(name);
    }


}
