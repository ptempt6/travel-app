package com.example.travelapp.service;

import com.example.travelapp.exception.EntityNotFoundException;
import com.example.travelapp.model.Place;
import com.example.travelapp.model.Route;
import com.example.travelapp.model.dto.request.PlaceRequestDto;
import com.example.travelapp.model.dto.response.PlaceResponseDto;
import com.example.travelapp.repository.PlaceRepository;
import com.example.travelapp.repository.RouteRepository;
import com.example.travelapp.service.mapper.PlaceMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final RouteRepository routeRepository;

    public PlaceService(PlaceRepository placeRepository,
                        PlaceMapper placeMapper, RouteRepository routeRepository) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
        this.routeRepository = routeRepository;
    }

    public List<PlaceResponseDto> getAllPlaces() {
        return placeRepository.findAll().stream()
                .map(placeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public PlaceResponseDto getPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));
        return placeMapper.toResponseDto(place);
    }

    @Transactional
    public PlaceResponseDto createPlace(PlaceRequestDto dto) {
        Place place = placeMapper.toEntity(dto);
        return placeMapper.toResponseDto(placeRepository.save(place));
    }

    @Transactional
    public PlaceResponseDto updatePlace(Long id, PlaceRequestDto dto) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));
        place.setName(dto.getName());
        place.setAddress(dto.getAddress());
        place.setDescription(dto.getDescription());
        return placeMapper.toResponseDto(placeRepository.save(place));
    }

    @Transactional
    public void deletePlace(Long id) {

        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));

        for (Route route : place.getRoutes()) {
            route.getPlaces().remove(place);
            routeRepository.save(route);
        }

        placeRepository.deleteById(id);
    }
}
