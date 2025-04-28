package ru.iguana.weatherservicespringboot.data.repository;

import ru.iguana.weatherservicespringboot.data.model.City;

import java.util.Collection;
import java.util.Optional;

public interface CityRepository {

    Collection<City> findAll();

    Optional<City> findOneByName(String name);

    void save(City city);

    void delete(City city);
}
