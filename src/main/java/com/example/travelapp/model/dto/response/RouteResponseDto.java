package com.example.travelapp.model.dto.response;

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
public class RouteResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long authorId;
    private List<PlaceResponseDto> places = new ArrayList<>();
}
