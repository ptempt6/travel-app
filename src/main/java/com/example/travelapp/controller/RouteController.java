package com.example.travelapp.controller;


import com.example.travelapp.model.dto.request.RouteRequestDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import com.example.travelapp.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@Tag(name = "Routes", description = "Operations related to routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @Operation(
            summary = "Get all routes",
            description = "Retrieve a list of all routes.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "List of routes retrieved successfully"),
                @ApiResponse(responseCode = "500",
                        description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                    " \"status\": 500, \"message\": \"Internal server error\", "
                                            +
                                            "\"path\": \"/routes\" }")))
            }
    )
    @GetMapping
    public ResponseEntity<List<RouteResponseDto>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @Operation(
            summary = "Get routes with more than a certain number of places",
            description = "Retrieve routes that have more than the specified number of places.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "List of routes retrieved successfully"),
                @ApiResponse(responseCode = "400",
                        description = "Invalid minPlaces value",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                   "\"status\": 400, \"message\": \"Invalid minPlaces value\","
                                            +
                                            " \"path\": \"/routes/more-than\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                  " \"status\": 500, \"message\": \"Internal server error\","
                                            +
                                            " \"path\": \"/routes/more-than\" }")))
            }
    )
    @GetMapping("/more-than")
    public ResponseEntity<List<RouteResponseDto>> getMoreThanRoutes(
            @RequestParam @Min(1) int minPlaces) {
        return ResponseEntity.ok(routeService.getAllRoutesWithMinimumPlaces(minPlaces));
    }

    @Operation(
            summary = "Get route by ID",
            description = "Retrieve the details of a specific route by its ID.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Route details retrieved successfully"),
                @ApiResponse(responseCode = "404",
                        description = "Route not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                            " \"status\": 404, \"message\": \"Route not found\","
                                            +
                                            " \"path\": \"/routes/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                    " \"status\": 500, \"message\": \"Internal server error\","
                                            +
                                            " \"path\": \"/routes/123\" }")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RouteResponseDto> getRouteById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @Operation(
            summary = "Create a new route",
            description = "Create a new route using the provided details.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Route created successfully"),
                @ApiResponse(responseCode = "400",
                        description = "Invalid input",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                            "\"status\": 400, \"message\": \"Invalid input\", "
                                            +
                                            "\"path\": \"/routes\" }"))),
                @ApiResponse(responseCode = "500",
                        description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                       "\"status\": 500, \"message\": \"Internal server error\", "
                                            +
                                            "\"path\": \"/routes\" }")))
            }
    )
    @PostMapping
    public ResponseEntity<RouteResponseDto> createRoute(@RequestBody @Valid RouteRequestDto dto) {
        return ResponseEntity.ok(routeService.createRoute(dto));
    }

    @Operation(
            summary = "Update an existing route",
            description = "Update the details of an existing route using the provided ID and data.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Route updated successfully"),
                @ApiResponse(responseCode = "404", description = "Route not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                            "\"status\": 404, \"message\": \"Route not found\","
                                            +
                                            " \"path\": \"/routes/123\" }"))),
                @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 400,"
                                            +
                                            " \"message\": \"Invalid input\","
                                            +
                                            " \"path\": \"/routes/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                  " \"status\": 500, \"message\": \"Internal server error\", "
                                            +
                                            "\"path\": \"/routes/123\" }")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RouteResponseDto> updateRoute(@PathVariable Long id,
                                                        @RequestBody @Valid RouteRequestDto dto) {
        return ResponseEntity.ok(routeService.updateRoute(id, dto));
    }

    @Operation(
            summary = "Delete a route",
            description = "Delete a specific route by its ID.",
            responses = {
                @ApiResponse(responseCode = "204", description = "Route deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Route not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                            "\"status\": 404, \"message\": \"Route not found\","
                                            +
                                            " \"path\": \"/routes/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                     " \"status\": 500, \"message\": \"Internal server error\","
                                            +
                                            " \"path\": \"/routes/123\" }")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
