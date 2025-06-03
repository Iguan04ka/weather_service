package ru.iguana.weatherservicespringboot.api.service;

import ru.iguana.weatherservicespringboot.data.entity.CityEntity;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;

import java.util.List;

public interface CityWeatherBuilderService {
    CityEntity buildAndSaveCityWithWeather(String cityName, String date);
    List<WeatherModel> fetchMeteoApi(String cityName, String date);
}
