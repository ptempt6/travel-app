package com.example.travelapp.model.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private List<RouteResponseDto> routes = new ArrayList<>();
}
