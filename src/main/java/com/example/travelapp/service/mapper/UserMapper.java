package com.example.travelapp.service.mapper;

import com.example.travelapp.model.User;
import com.example.travelapp.model.dto.request.UserRequestDto;
import com.example.travelapp.model.dto.response.UserResponseDto;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RouteMapper routeMapper;

    public UserMapper(RouteMapper routeMapper) {
        this.routeMapper = routeMapper;
    }

    public User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoutes(user.getRoutes().stream()
                .map(routeMapper::toResponseDto)
                .collect(Collectors.toList()));
        return dto;
    }
}
