package com.example.travelapp.model.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceRequestDto {
    private String name;
    private String address;
    private String description;
}
