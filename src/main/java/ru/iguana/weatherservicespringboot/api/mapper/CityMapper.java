package ru.iguana.weatherservicespringboot.api.mapper;

import org.mapstruct.Mapper;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.data.entity.CityEntity;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityEntity toEntity(CityDto cityDto);

    CityDto toDto(CityEntity cityEntity);
}
