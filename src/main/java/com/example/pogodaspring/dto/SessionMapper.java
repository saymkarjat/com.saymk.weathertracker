package com.example.pogodaspring.dto;

import com.example.pogodaspring.model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    @Mapping(source = "user.login", target = "userLogin")
    SessionDTO sessionToSessionDTO(Session session);

    @Mapping(source = "userLogin", target = "user.login")
    Session sessionDtoToSession(SessionDTO sessionDTO);

}
