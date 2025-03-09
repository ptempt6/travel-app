package com.example.travelapp.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceResponseDto {
    private Long id;
    private String name;
    private String address;
    private String description;
}
