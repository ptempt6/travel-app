package com.example.travelapp.controller;


import com.example.travelapp.model.dto.request.RouteRequestDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import com.example.travelapp.service.RouteService;
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
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RouteResponseDto>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/more-than")
    public ResponseEntity<List<RouteResponseDto>> getMoreThanRoutes(
            @RequestParam int minPlaces) {
        return ResponseEntity.ok(routeService.getAllRoutesWithMinimumPlaces(minPlaces));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponseDto> getRouteById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PostMapping
    public ResponseEntity<RouteResponseDto> createRoute(@RequestBody RouteRequestDto dto) {
        return ResponseEntity.ok(routeService.createRoute(dto));
    }

    @PostMapping("{routeId}/add/{placeId}")
    public ResponseEntity<Void> addPlaceToRoute(@PathVariable Long routeId,
                                                @PathVariable Long placeId) {
        routeService.addPlaceToRoute(routeId, placeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponseDto> updateRoute(@PathVariable Long id,
                                                        @RequestBody RouteRequestDto dto) {
        return ResponseEntity.ok(routeService.updateRoute(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{routeId}/remove/{placeId}")
    public ResponseEntity<Void> removePlaceFromRoute(@PathVariable Long routeId,
                                                     @PathVariable Long placeId) {
        routeService.removePlaceFromRoute(routeId, placeId);
        return ResponseEntity.noContent().build();
    }
}
