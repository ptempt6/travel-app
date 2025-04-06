package com.example.travelapp.service;

import com.example.travelapp.exception.BadRequestException;
import com.example.travelapp.exception.ErrorMessages;
import com.example.travelapp.exception.NotFoundException;
import com.example.travelapp.model.*;
import com.example.travelapp.model.dto.request.RouteRequestDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import com.example.travelapp.repository.*;
import com.example.travelapp.service.mapper.RouteMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RouteServiceUnitTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private RouteMapper routeMapper;

    @InjectMocks
    private RouteService routeService;

    @Test
    void getAllRoutes_ShouldReturnAllRoutes() {
        // Arrange
        Route route = new Route();
        RouteResponseDto dto = new RouteResponseDto();
        when(routeRepository.findAll()).thenReturn(List.of(route));
        when(routeMapper.toResponseDto(route)).thenReturn(dto);

        // Act
        List<RouteResponseDto> result = routeService.getAllRoutes();

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(routeRepository).findAll();
    }

    @Test
    void getAllRoutesWithMinimumPlaces_ShouldReturnFilteredRoutes() {
        // Arrange
        int minPlaces = 2;
        Route route = new Route();
        route.setPlaces(Arrays.asList(new Place(), new Place()));
        RouteResponseDto dto = new RouteResponseDto();

        when(routeRepository.findRoutesWithMinimumPlaces(minPlaces)).thenReturn(List.of(route));
        when(routeMapper.toResponseDto(route)).thenReturn(dto);

        // Act
        List<RouteResponseDto> result = routeService.getAllRoutesWithMinimumPlaces(minPlaces);

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(routeRepository).findRoutesWithMinimumPlaces(minPlaces);
    }

    @Test
    void getRouteById_WhenExists_ShouldReturnRoute() {
        // Arrange
        Long id = 1L;
        Route route = new Route();
        RouteResponseDto dto = new RouteResponseDto();

        when(routeRepository.findById(id)).thenReturn(Optional.of(route));
        when(routeMapper.toResponseDto(route)).thenReturn(dto);

        // Act
        RouteResponseDto result = routeService.getRouteById(id);

        // Assert
        assertEquals(dto, result);
        verify(routeRepository).findById(id);
    }

    @Test
    void getRouteById_WhenNotExists_ShouldThrowException() {
        // Arrange
        Long id = 1L;
        when(routeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> routeService.getRouteById(id));

        assertEquals(ErrorMessages.ROUTE_NOT_FOUND, exception.getMessage());
    }

    @Test
    void createRoute_ShouldCreateNewRoute() {
        // Arrange
        Long authorId = 1L;
        List<Long> placeIds = List.of(1L, 2L);
        RouteRequestDto dto = new RouteRequestDto("Test Route", "Description", authorId, placeIds);

        User author = new User();
        List<Place> places = Arrays.asList(new Place(), new Place());
        Route route = new Route();
        Route savedRoute = new Route();
        RouteResponseDto responseDto = new RouteResponseDto();

        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(placeRepository.findAllById(placeIds)).thenReturn(places);
        when(routeMapper.toEntity(dto, author, places)).thenReturn(route);
        when(routeRepository.save(route)).thenReturn(savedRoute);
        when(routeMapper.toResponseDto(savedRoute)).thenReturn(responseDto);

        // Act
        RouteResponseDto result = routeService.createRoute(dto);

        // Assert
        assertEquals(responseDto, result);
        verify(routeRepository).save(route);
    }

    @Test
    void createRoute_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        Long authorId = 1L;
        RouteRequestDto dto = new RouteRequestDto("Test Route", "Description", authorId, List.of());
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> routeService.createRoute(dto));

        assertEquals(ErrorMessages.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void updateRoute_ShouldUpdateRoute() {
        // Arrange
        Long id = 1L;
        Long newAuthorId = 2L;
        RouteRequestDto dto = new RouteRequestDto("New Name", "New Desc", newAuthorId, null);

        Route route = new Route();
        User newAuthor = new User();
        Route savedRoute = new Route();
        RouteResponseDto responseDto = new RouteResponseDto();

        when(routeRepository.findById(id)).thenReturn(Optional.of(route));
        when(userRepository.findById(newAuthorId)).thenReturn(Optional.of(newAuthor));
        when(routeRepository.save(route)).thenReturn(savedRoute);
        when(routeMapper.toResponseDto(savedRoute)).thenReturn(responseDto);

        // Act
        RouteResponseDto result = routeService.updateRoute(id, dto);

        // Assert
        assertEquals("New Name", route.getName());
        assertEquals("New Desc", route.getDescription());
        assertEquals(newAuthor, route.getAuthor());
        assertEquals(responseDto, result);
    }

    @Test
    void addPlaceToRoute_ShouldAddPlace() {
        // Arrange
        Long routeId = 1L;
        Long placeId = 1L;

        Route route = new Route();
        route.setPlaces(new ArrayList<>());
        Place place = new Place();

        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        // Act
        routeService.addPlaceToRoute(routeId, placeId);

        // Assert
        assertTrue(route.getPlaces().contains(place));
        verify(routeRepository).save(route);
    }

    @Test
    void addPlaceToRoute_WhenPlaceAlreadyExists_ShouldThrowException() {
        // Arrange
        Long routeId = 1L;
        Long placeId = 1L;

        Route route = new Route();
        Place place = new Place();
        route.setPlaces(new ArrayList<>(List.of(place)));

        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> routeService.addPlaceToRoute(routeId, placeId));

        assertEquals("Place already exists in this route", exception.getMessage());
    }

    @Test
    void removePlaceFromRoute_ShouldRemovePlace() {
        // Arrange
        Long routeId = 1L;
        Long placeId = 1L;

        Route route = new Route();
        Place place = new Place();
        route.setPlaces(new ArrayList<>(List.of(place)));

        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        // Act
        routeService.removePlaceFromRoute(routeId, placeId);

        // Assert
        assertFalse(route.getPlaces().contains(place));
        verify(routeRepository).save(route);
    }

    @Test
    void deleteRoute_ShouldDeleteRoute() {
        // Arrange
        Long id = 1L;

        // Act
        routeService.deleteRoute(id);

        // Assert
        verify(routeRepository).deleteById(id);
    }

    @Test
    void addPlaceToRoute_WhenRouteNotFound_ShouldThrowNotFoundException() {
        // Arrange
        Long nonExistentRouteId = 999L;
        Long placeId = 1L;

        when(routeRepository.findById(nonExistentRouteId))
                .thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> routeService.addPlaceToRoute(nonExistentRouteId, placeId));

        assertEquals(ErrorMessages.ROUTE_NOT_FOUND, exception.getMessage());
        verify(routeRepository, never()).save(any());
    }

    @Test
    void addPlaceToRoute_WhenPlaceNotFound_ShouldThrowNotFoundException() {
        // Arrange
        Long routeId = 1L;
        Long nonExistentPlaceId = 999L;
        Route existingRoute = new Route();

        when(routeRepository.findById(routeId))
                .thenReturn(Optional.of(existingRoute));
        when(placeRepository.findById(nonExistentPlaceId))
                .thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> routeService.addPlaceToRoute(routeId, nonExistentPlaceId));

        assertEquals(ErrorMessages.PLACE_NOT_FOUND, exception.getMessage());
        verify(routeRepository, never()).save(any());
    }

    @Test
    void removePlaceFromRoute_WhenRouteNotExists_ShouldThrowNotFoundException() {
        // Arrange
        Long routeId = 1L;
        Long placeId = 1L;

        when(routeRepository.findById(routeId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> routeService.removePlaceFromRoute(routeId, placeId));

        assertEquals(ErrorMessages.ROUTE_NOT_FOUND, exception.getMessage());
        verify(routeRepository, never()).save(any());
    }

    @Test
    void removePlaceFromRoute_WhenPlaceNotExists_ShouldThrowNotFoundException() {
        // Arrange
        Long routeId = 1L;
        Long placeId = 1L;

        Route route = new Route();
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(placeRepository.findById(placeId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> routeService.removePlaceFromRoute(routeId, placeId));

        assertEquals(ErrorMessages.PLACE_NOT_FOUND, exception.getMessage());
        verify(routeRepository, never()).save(any());
    }
}