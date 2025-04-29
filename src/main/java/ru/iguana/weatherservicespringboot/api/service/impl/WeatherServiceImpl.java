package ru.iguana.weatherservicespringboot.api.service.impl;

import org.springframework.stereotype.Service;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;
import ru.iguana.weatherservicespringboot.data.model.City;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;

import java.util.Collection;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final CityRepository repository;

    public WeatherServiceImpl(CityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<City> findAll() {
        return repository.findAll();
    }

    @Override
    public City findOneByName(String name) {
        return repository.findOneByName(name)
                .orElseThrow(() -> new IllegalArgumentException("City not found: " + name));
    }

    @Override
    public void create(String name) {
        repository.save(new City(name));
    }

    @Override
    public void delete(String name) {
        repository.delete(new City(name));
    }
}

