package com.example.travelapp.controller;


import com.example.travelapp.model.dto.request.PlaceRequestDto;
import com.example.travelapp.model.dto.response.PlaceResponseDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import com.example.travelapp.service.PlaceService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceResponseDto>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @GetMapping("/not-visited")
    public ResponseEntity<List<PlaceResponseDto>> getNotVisitedPlaces(@RequestParam Long userId) {
        List<PlaceResponseDto> places = placeService.findPlacesNotVisitedByUser(userId);
        return ResponseEntity.ok(places);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @GetMapping("/{id}/routes")
    public ResponseEntity<List<RouteResponseDto>> getRoutesByPlace(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getRoutesByPlace(id));
    }

    @PostMapping
    public ResponseEntity<PlaceResponseDto> createPlace(@RequestBody PlaceRequestDto dto) {
        return ResponseEntity.ok(placeService.createPlace(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> updatePlace(@PathVariable Long id,
                                                        @RequestBody PlaceRequestDto dto) {
        return ResponseEntity.ok(placeService.updatePlace(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
