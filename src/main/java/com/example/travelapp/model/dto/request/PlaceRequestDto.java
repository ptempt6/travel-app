package com.example.travelapp.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for creating a place")
public class PlaceRequestDto {

    @NotNull(message = "name is required")
    @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters")
    @JsonProperty(required = true)
    @Schema(
            description = "Place name",
            example = "Mountain Peak",
            minLength = 3,
            maxLength = 25
    )
    private String name;

    @NotNull(message = "address is required")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    @JsonProperty(required = true)
    @Schema(
            description = "Place address",
            example = "123 Mountain Road, Peak City",
            minLength = 5,
            maxLength = 100
    )
    private String address;

    @Size(max = 1024, message = "Description must be less than 1024 characters")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty(defaultValue = "")
    @Schema(
            description = "Place description",
            example = "The highest point of the mountain, offering breathtaking views",
            maxLength = 1024
    )
    private String description;
}
