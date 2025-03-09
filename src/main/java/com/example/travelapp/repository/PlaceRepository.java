package com.example.travelapp.repository;

import com.example.travelapp.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    //Place findByName(String name);
}
