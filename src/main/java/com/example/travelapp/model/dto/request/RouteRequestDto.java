package com.example.travelapp.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "DTO for creating a route")
public class RouteRequestDto {

    @NotNull(message = "name is required")
    @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters")
    @JsonProperty(required = true)
    @Schema(
            description = "Route name",
            example = "Mountain Adventure",
            minLength = 3,
            maxLength = 25
    )
    private String name;

    @Size(max = 1024, message = "Description must be less than 1024 characters")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty(defaultValue = "")
    @Schema(
            description = "Route description",
            example = "A beautiful mountain route with scenic views",
            maxLength = 1024
    )
    private String description;

    @NotNull(message = "authorId is required")
    @JsonProperty(required = true)
    @Schema(
            description = "ID of the author of the route",
            example = "1"
    )
    private Long authorId;

    @Schema(
            description = "List of place IDs associated with the route",
            example = "[101, 102, 103]"
    )
    private List<Long> placeIds = new ArrayList<>();
}
