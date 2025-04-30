package ru.iguana.weatherservicespringboot.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iguana.weatherservicespringboot.data.entity.WeatherEntity;

public interface WeatherRepository extends JpaRepository<WeatherEntity, Integer> {
}
