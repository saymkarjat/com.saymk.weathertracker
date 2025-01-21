package com.example.pogodaspring.weather.dto;

import com.example.pogodaspring.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    Location toEntity(LocationDTO locationDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "latitude", target = "lat")
    @Mapping(source = "longitude", target = "lon")
    LocationDTO toDto(Location location);

    List<Location> toEntityList(List<LocationDTO> locationDTOList);

    List<LocationDTO> toDtoList(List<Location> locationList);

}