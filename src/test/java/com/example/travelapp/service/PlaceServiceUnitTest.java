package com.example.travelapp.service;

import com.example.travelapp.exception.ErrorMessages;
import com.example.travelapp.exception.NotFoundException;
import com.example.travelapp.model.Place;
import com.example.travelapp.model.Route;
import com.example.travelapp.model.dto.request.PlaceRequestDto;
import com.example.travelapp.model.dto.response.PlaceResponseDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import com.example.travelapp.repository.PlaceRepository;
import com.example.travelapp.repository.RouteRepository;
import com.example.travelapp.repository.UserRepository;
import com.example.travelapp.service.mapper.PlaceMapper;
import com.example.travelapp.service.mapper.RouteMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceUnitTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PlaceMapper placeMapper;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private RouteMapper routeMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PlaceService placeService;

    @Test
    void getAllPlaces_ShouldReturnListOfPlaces() {
        // Arrange
        Place place = new Place();
        PlaceResponseDto dto = new PlaceResponseDto();
        when(placeRepository.findAll()).thenReturn(List.of(place));
        when(placeMapper.toResponseDto(place)).thenReturn(dto);

        // Act
        List<PlaceResponseDto> result = placeService.getAllPlaces();

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(placeRepository).findAll();
    }

    @Test
    void findPlacesNotVisitedByUser_WhenUserExists_ShouldReturnPlaces() {
        // Arrange
        Long userId = 1L;
        Place place = new Place();
        PlaceResponseDto dto = new PlaceResponseDto();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(placeRepository.findPlacesNotVisitedByUser(userId)).thenReturn(List.of(place));
        when(placeMapper.toResponseDto(place)).thenReturn(dto);

        // Act
        List<PlaceResponseDto> result = placeService.findPlacesNotVisitedByUser(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(userRepository).existsById(userId);
    }

    @Test
    void findPlacesNotVisitedByUser_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> placeService.findPlacesNotVisitedByUser(userId));

        assertEquals(ErrorMessages.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getPlaceById_WhenPlaceExists_ShouldReturnPlace() {
        // Arrange
        Long id = 1L;
        Place place = new Place();
        PlaceResponseDto dto = new PlaceResponseDto();

        when(placeRepository.findById(id)).thenReturn(Optional.of(place));
        when(placeMapper.toResponseDto(place)).thenReturn(dto);

        // Act
        PlaceResponseDto result = placeService.getPlaceById(id);

        // Assert
        assertEquals(dto, result);
        verify(placeRepository).findById(id);
    }

    @Test
    void getPlaceById_WhenPlaceNotExists_ShouldThrowException() {
        // Arrange
        Long id = 1L;
        when(placeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> placeService.getPlaceById(id));

        assertEquals(ErrorMessages.PLACE_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getRoutesByPlace_WhenPlaceExists_ShouldReturnRoutes() {
        // Arrange
        Long id = 1L;
        Place place = new Place();
        Route route = new Route();
        RouteResponseDto dto = new RouteResponseDto();
        place.setRoutes(List.of(route));

        when(placeRepository.findById(id)).thenReturn(Optional.of(place));
        when(routeMapper.toResponseDto(route)).thenReturn(dto);

        // Act
        List<RouteResponseDto> result = placeService.getRoutesByPlace(id);

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(placeRepository).findById(id);
    }

    @Test
    void createPlace_ShouldSaveAndReturnPlace() {
        // Arrange
        PlaceRequestDto requestDto = new PlaceRequestDto();
        Place place = new Place();
        Place savedPlace = new Place();
        PlaceResponseDto responseDto = new PlaceResponseDto();

        when(placeMapper.toEntity(requestDto)).thenReturn(place);
        when(placeRepository.save(place)).thenReturn(savedPlace);
        when(placeMapper.toResponseDto(savedPlace)).thenReturn(responseDto);

        // Act
        PlaceResponseDto result = placeService.createPlace(requestDto);

        // Assert
        assertEquals(responseDto, result);
        verify(placeRepository).save(place);
    }

    @Test
    void updatePlace_WhenPlaceExists_ShouldUpdateAndReturnPlace() {
        // Arrange
        Long id = 1L;
        PlaceRequestDto requestDto = new PlaceRequestDto("New Name", "New Address", "New Desc");
        Place place = new Place();
        PlaceResponseDto responseDto = new PlaceResponseDto();

        when(placeRepository.findById(id)).thenReturn(Optional.of(place));
        when(placeRepository.save(place)).thenReturn(place);
        when(placeMapper.toResponseDto(place)).thenReturn(responseDto);

        // Act
        PlaceResponseDto result = placeService.updatePlace(id, requestDto);

        // Assert
        assertEquals("New Name", place.getName());
        assertEquals("New Address", place.getAddress());
        assertEquals("New Desc", place.getDescription());
        assertEquals(responseDto, result);
        verify(placeRepository).save(place);
    }

    @Test
    void deletePlace_WhenPlaceExists_ShouldDeletePlace() {
        // Arrange
        Long id = 1L;
        Place place = new Place();
        Route route = new Route();

        // Создаем изменяемые коллекции
        place.setRoutes(new ArrayList<>(List.of(route)));
        route.setPlaces(new ArrayList<>(List.of(place)));

        when(placeRepository.findById(id)).thenReturn(Optional.of(place));
        when(routeRepository.save(route)).thenReturn(route);

        // Act
        placeService.deletePlace(id);

        // Assert
        verify(routeRepository).save(route);
        verify(placeRepository).deleteById(id);
        assertTrue(route.getPlaces().isEmpty());
    }
}
