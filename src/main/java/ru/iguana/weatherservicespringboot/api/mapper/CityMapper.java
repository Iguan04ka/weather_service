package ru.iguana.weatherservicespringboot.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.dto.WeatherDto;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;
import ru.iguana.weatherservicespringboot.data.entity.WeatherEntity;

@Mapper(componentModel = "spring")
public interface CityMapper {
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "weather", target = "weather")
    CityEntity toEntity(CityDto cityDto);

//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "weather", target = "weather")
    CityDto toDto(CityEntity cityEntity);
}
