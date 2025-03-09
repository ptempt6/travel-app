package com.example.travelapp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RouteRequestDto {
    private String name;
    private String description;
    private Long authorId;
    private List<Long> placeIds = new ArrayList<>();
}
