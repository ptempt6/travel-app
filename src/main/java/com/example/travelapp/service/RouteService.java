package com.example.travelapp.service;

import com.example.travelapp.exception.EntityNotFoundException;
import com.example.travelapp.model.Place;
import com.example.travelapp.model.Route;
import com.example.travelapp.model.User;
import com.example.travelapp.model.dto.request.RouteRequestDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import com.example.travelapp.repository.PlaceRepository;
import com.example.travelapp.repository.RouteRepository;
import com.example.travelapp.repository.UserRepository;
import com.example.travelapp.service.mapper.RouteMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final RouteMapper routeMapper;

    public RouteService(RouteRepository routeRepository,
                        UserRepository userRepository,
                        PlaceRepository placeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.routeMapper = routeMapper;
    }

    public List<RouteResponseDto> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(routeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public RouteResponseDto getRouteById(Long id) {
        Route route = routeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Route not found"));
        return routeMapper.toResponseDto(route);
    }

    @Transactional
    public RouteResponseDto createRoute(RouteRequestDto dto) {
        User author = userRepository.findById(dto.getAuthorId()).orElseThrow(()
                -> new EntityNotFoundException("User not found"));
        List<Place> places = placeRepository.findAllById(dto.getPlaceIds());
        Route route = routeMapper.toEntity(dto, author, places);
        return routeMapper.toResponseDto(routeRepository.save(route));
    }

    @Transactional
    public RouteResponseDto updateRoute(Long id, RouteRequestDto dto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found"));

        route.setName(dto.getName());
        route.setDescription(dto.getDescription());

        if (dto.getAuthorId() != null) {
            User author = userRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            route.setAuthor(author);
        }

        route = routeRepository.save(route);
        return routeMapper.toResponseDto(route);
    }

    @Transactional
    public void addPlaceToRoute(Long routeId, Long placeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() ->
                new EntityNotFoundException("Route not found"));
        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new EntityNotFoundException("Place not found"));
        if (route.getPlaces().contains(place)) {
            throw new IllegalArgumentException("Place already exists in this route");
        }

        route.getPlaces().add(place);
        routeRepository.save(route);
    }

    @Transactional
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    @Transactional
    public void removePlaceFromRoute(Long routeId, Long placeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Route not found"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));

        if (!route.getPlaces().contains(place)) {
            throw new IllegalArgumentException("Place is not in this route");
        }

        route.getPlaces().remove(place);
        routeRepository.save(route);
    }
}
