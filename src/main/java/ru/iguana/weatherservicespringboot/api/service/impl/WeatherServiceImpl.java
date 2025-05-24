package ru.iguana.weatherservicespringboot.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.mapper.CityMapper;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    @Override
    public void saveCity(String name) {
        CityDto cityDto = new CityDto();
        cityDto.setName(name);
        try {
            log.info("Created by cityDto on request: " + cityDto);

            cityRepository.save(cityMapper.toEntity(cityDto));

            log.info(cityDto + " successfully saved");
        }
        catch (Exception e){
            log.error("Error request to save cityDto " + cityDto + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CityDto> findAllCities() {
        try {
            log.info("All cities are searched in the database");
            List<CityDto> result = cityRepository.findAll()
                                                    .stream()
                                                    .map(cityMapper::toDto)
                                                    .toList();
            log.info("All cities found in the database");
            return result;
        }
        catch (Exception e){
            log.error("Error while searching for cities in the database: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public CityDto findCityByName(String name) {
        try{
            log.info("Search in the city database with the name " + name);

            CityDto result = cityMapper.toDto(cityRepository.findByName(name).orElseThrow());

            log.info("City with name " + name + " found in database");
            log.info("Made cityDto object: " + result);
            return result;
        }
        catch (Exception e){
            log.error("Error while searching city with name " + name + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteCity(String name) {
        try{
            log.info("Trying to delete city with name: " + name);

            cityRepository.deleteCityEntityByName(name);

            log.info("City with name " + name + " was deleted");
        }
        catch (Exception e){
            log.error("Error while trying to delete city with name " + name + ": " + e.getMessage());
            throw e;
        }
    }
}














