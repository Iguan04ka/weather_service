package ru.iguana.weatherservicespringboot.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.mapper.CityMapper;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    @Override
    public void saveCity(String name) {
        CityDto cityDto = new CityDto();
        cityDto.setName(name);
        cityRepository.save(cityMapper.toEntity(cityDto));
    }

    @Override
    public List<CityDto> findAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(cityMapper::toDto)
                .toList();
    }

    @Override
    public CityDto findCityByName(String name) {
        return cityMapper.toDto(cityRepository.findByName(name).orElseThrow());
    }

    @Override
    public void deleteCity(String name) {
        cityRepository.deleteCityEntityByName(name);
    }
}