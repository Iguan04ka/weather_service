package ru.iguana.weatherservicespringboot.api.service;

import ru.iguana.weatherservicespringboot.api.dto.CityDto;

import java.util.List;

public interface WeatherService {
    void deleteCityByName(String name);
    CityDto getForecastByCityName(String cityName);
    void saveCityAndHisWeatherForecast(String cityName, String date);
    void updateWeatherForecastByCityName(String cityName, String date);

}