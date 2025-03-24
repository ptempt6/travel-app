package com.example.travelapp.controller;


import com.example.travelapp.model.dto.request.PlaceRequestDto;
import com.example.travelapp.model.dto.response.PlaceResponseDto;
import com.example.travelapp.model.dto.response.RouteResponseDto;
import com.example.travelapp.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Places", description = "Operations related to places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @Operation(
            summary = "Get all places",
            description = "Retrieve a list of all places.",
            responses = {
                @ApiResponse(responseCode = "200",
                            description = "List of places retrieved successfully"),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                    " \"status\": 500, \"message\":"
                                            +
                                            " \"Internal server error\", \"path\": \"/places\" }")))
            }
    )
    @GetMapping
    public ResponseEntity<List<PlaceResponseDto>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @Operation(
            summary = "Get places not visited by a user",
            description = "Retrieve a list of places not yet visited by a specific user.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "List of places retrieved successfully"),
                @ApiResponse(responseCode = "400", description = "User ID is required",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                            " \"status\": 400, \"message\": \"User ID is required\","
                                            +
                                            " \"path\": \"/places/not-visited\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                         " \"status\": 500, \"message\": \"Internal server error\","
                                            +
                                            " \"path\": \"/places/not-visited\" }")))
            }
    )
    @GetMapping("/not-visited")
    public ResponseEntity<List<PlaceResponseDto>> getNotVisitedPlaces(@RequestParam Long userId) {
        return ResponseEntity.ok(placeService.findPlacesNotVisitedByUser(userId));
    }

    @Operation(
            summary = "Get a place by ID",
            description = "Retrieve the details of a specific place by its ID.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Place details retrieved successfully"),
                @ApiResponse(responseCode = "404", description = "Place not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                              "\"status\": 404, \"message\": \"Place not found\","
                                            +
                                            " \"path\": \"/places/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                    " \"status\": 500, \"message\": \"Internal server error\","
                                            +
                                            " \"path\": \"/places/123\" }")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @Operation(
            summary = "Get routes by place",
            description = "Retrieve a list of routes associated with a specific place.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "List of routes retrieved successfully"),
                @ApiResponse(responseCode = "404",
                        description = "Place not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 404,"
                                            +
                                            " \"message\": \"Place not found\","
                                            +
                                            " \"path\": \"/places/123/routes\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 500, "
                                            +
                                            "\"message\": \"Internal server error\","
                                            +
                                            " \"path\": \"/places/123/routes\" }")))
            }
    )
    @GetMapping("/{id}/routes")
    public ResponseEntity<List<RouteResponseDto>> getRoutesByPlace(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getRoutesByPlace(id));
    }

    @Operation(
            summary = "Create a new place",
            description = "Create a new place using the provided details.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Place created successfully"),
                @ApiResponse(responseCode = "400",
                        description = "Invalid input",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                            "\"status\": 400, \"message\": \"Invalid input\","
                                            +
                                            " \"path\": \"/places\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                              +    " \"status\": 500, \"message\": \"Internal server error\","
                                         +   " \"path\": \"/places\" }")))
            }
    )
    @PostMapping
    public ResponseEntity<PlaceResponseDto> createPlace(@RequestBody @Valid PlaceRequestDto dto) {
        return ResponseEntity.ok(placeService.createPlace(dto));
    }

    @Operation(
            summary = "Update an existing place",
            description = "Update the details of an existing place using the provided ID and data.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Place updated successfully"),
                @ApiResponse(responseCode = "404",
                        description = "Place not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\","
                                            +
                                            " \"status\": 404, \"message\": \"Place not found\", "
                                            +
                                            "\"path\": \"/places/123\" }"))),
                @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            + "\"status\": 400, \"message\": \"Invalid input\","
                                          +  " \"path\": \"/places/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 500,"
                                  + " \"message\": \"Internal server error\","
                                   +         " \"path\": \"/places/123\" }")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> updatePlace(@PathVariable Long id,
                                                        @RequestBody @Valid PlaceRequestDto dto) {
        return ResponseEntity.ok(placeService.updatePlace(id, dto));
    }

    @Operation(
            summary = "Delete a place",
            description = "Delete a specific place by its ID.",
            responses = {
                @ApiResponse(responseCode = "204",
                       description = "Place deleted successfully"),
                @ApiResponse(responseCode = "404",
                       description = "Place not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                            "\"status\": 404, \"message\": \"Place not found\", "
                                            +
                                            "\"path\": \"/places/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                      "\"status\": 500, \"message\": \"Internal server error\", "
                                            +
                                            "\"path\": \"/places/123\" }")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
