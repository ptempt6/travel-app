package com.example.travelapp.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for returning route details")
public class RouteResponseDto {

    @JsonProperty(required = true)
    @Schema(
            description = "Route ID",
            example = "10"
    )
    private Long id;

    @Schema(
            description = "Route name",
            example = "Mountain Adventure"
    )
    private String name;

    @Schema(
            description = "Route description",
            example = "A beautiful mountain route with scenic views"
    )
    private String description;

    @JsonProperty(required = true)
    @Schema(
            description = "ID of the author of the route",
            example = "1"
    )
    private Long authorId;

    @Schema(
            description = "List of places associated with the route",
            example = "[{\"id\": 101, \"name\": "
                    +
                    "\"Mountain Peak\"}, {\"id\": 102, \"name\": \"Lake View\"}]"
    )
    private List<PlaceResponseDto> places = new ArrayList<>();
}
