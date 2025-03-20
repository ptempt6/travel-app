package com.example.travelapp.repository;

import com.example.travelapp.model.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query("SELECT p FROM Place p WHERE p.id NOT IN "
            + "(SELECT DISTINCT pl.id FROM Place pl "
            + "JOIN pl.routes r WHERE r.author.id = :userId)")
    List<Place> findPlacesNotVisitedByUser(@Param("userId") Long userId);
}
