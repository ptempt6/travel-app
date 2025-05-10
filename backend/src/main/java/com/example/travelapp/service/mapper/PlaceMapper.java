package com.example.travelapp.service.mapper;

import com.example.travelapp.model.Place;
import com.example.travelapp.model.dto.request.PlaceRequestDto;
import com.example.travelapp.model.dto.response.PlaceResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper {

    public Place toEntity(PlaceRequestDto dto) {
        Place place = new Place();
        place.setName(dto.getName());
        place.setAddress(dto.getAddress());
        place.setDescription(dto.getDescription());
        return place;
    }

    public PlaceResponseDto toResponseDto(Place place) {
        PlaceResponseDto dto = new PlaceResponseDto();
        dto.setId(place.getId());
        dto.setName(place.getName());
        dto.setAddress(place.getAddress());
        dto.setDescription(place.getDescription());
        return dto;
    }
}
