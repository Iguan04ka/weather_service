package ru.iguana.weatherservicespringboot.api.service;

import ru.iguana.weatherservicespringboot.data.model.City;

import java.util.Collection;
import java.util.Optional;

public interface WeatherService {

    Collection<City> findAll();

    City findOneByName(String name);

    void create(String name);

    void delete(String name);
}

