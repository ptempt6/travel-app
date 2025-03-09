package com.example.travelapp.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private List<RouteResponseDto> routes = new ArrayList<>();
}
