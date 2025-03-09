package com.example.travelapp.service.mapper;

import com.example.travelapp.model.Place;
import com.example.travelapp.model.Route;
import com.example.travelapp.model.User;
import com.example.travelapp.model.dto.request.RouteRequestDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RouteMapper {

    private final PlaceMapper placeMapper;

    public RouteMapper(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    public RouteResponseDto toResponseDto(Route route) {
        return new RouteResponseDto(
                route.getId(),
                route.getName(),
                route.getDescription(),
                route.getAuthor() != null ? route.getAuthor().getId() : null,
                route.getPlaces().stream()
                        .map(placeMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
    }

    public Route toEntity(RouteRequestDto dto, User author, List<Place> places) {
        Route route = new Route();
        route.setName(dto.getName());
        route.setDescription(dto.getDescription());
        route.setAuthor(author);
        route.setPlaces(places);
        return route;
    }
}

