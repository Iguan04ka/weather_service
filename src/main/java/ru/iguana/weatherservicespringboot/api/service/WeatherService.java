package ru.iguana.weatherservicespringboot.api.service;

import ru.iguana.weatherservicespringboot.api.dto.CityDto;

import java.util.List;

public interface WeatherService {
    void saveCity(String name);
    List<CityDto> findAllCities();
    CityDto findCityByName(String name);
    void deleteCity(String name);
}