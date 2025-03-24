package com.example.travelapp.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for returning place details")
public class PlaceResponseDto {

    @Schema(
            description = "Place ID",
            example = "101"
    )
    private Long id;

    @Schema(
            description = "Place name",
            example = "Mountain Peak"
    )
    private String name;

    @Schema(
            description = "Place address",
            example = "123 Mountain Road, Peak City"
    )
    private String address;

    @Schema(
            description = "Place description",
            example = "The highest point of the mountain, offering breathtaking views"
    )
    private String description;
}
