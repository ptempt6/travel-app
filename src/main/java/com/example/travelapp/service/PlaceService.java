package com.example.travelapp.service;

import com.example.travelapp.exception.EntityNotFoundException;
import com.example.travelapp.exception.ErrorMessages;
import com.example.travelapp.model.Place;
import com.example.travelapp.model.Route;
import com.example.travelapp.model.dto.request.PlaceRequestDto;
import com.example.travelapp.model.dto.response.PlaceResponseDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import com.example.travelapp.repository.PlaceRepository;
import com.example.travelapp.repository.RouteRepository;
import com.example.travelapp.service.mapper.PlaceMapper;
import com.example.travelapp.service.mapper.RouteMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    public PlaceService(PlaceRepository placeRepository,
                        PlaceMapper placeMapper, RouteRepository routeRepository,
                        RouteMapper routeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    public List<PlaceResponseDto> getAllPlaces() {
        return placeRepository.findAll().stream()
                .map(placeMapper::toResponseDto)
                .toList();
    }

    public PlaceResponseDto getPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.PLACE_NOT_FOUND));
        return placeMapper.toResponseDto(place);
    }

    public List<RouteResponseDto> getRoutesByPlace(Long id) {
        Place place = placeRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(ErrorMessages.PLACE_NOT_FOUND));
        return place.getRoutes().stream().map(routeMapper::toResponseDto).toList();
    }

    @Transactional
    public PlaceResponseDto createPlace(PlaceRequestDto dto) {
        Place place = placeMapper.toEntity(dto);
        return placeMapper.toResponseDto(placeRepository.save(place));
    }

    @Transactional
    public PlaceResponseDto updatePlace(Long id, PlaceRequestDto dto) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.PLACE_NOT_FOUND));
        place.setName(dto.getName());
        place.setAddress(dto.getAddress());
        place.setDescription(dto.getDescription());
        return placeMapper.toResponseDto(placeRepository.save(place));
    }

    @Transactional
    public void deletePlace(Long id) {

        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.PLACE_NOT_FOUND));

        for (Route route : place.getRoutes()) {
            route.getPlaces().remove(place);
            routeRepository.save(route);
        }

        placeRepository.deleteById(id);
    }
}
