package ru.iguana.weatherservicespringboot.data.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;

import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {
    Optional<CityEntity> findByName(String name);
    @Transactional
    void deleteCityEntityByName(String name);

}