package ru.iguana.weatherservicespringboot.api.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.iguana.weatherservicespringboot.api.dto.WeatherDto;
import ru.iguana.weatherservicespringboot.data.entity.WeatherEntity;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    WeatherEntity toEntity(WeatherDto weatherDto);

    WeatherDto toDto(WeatherEntity weatherEntity);
}
