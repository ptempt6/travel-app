package com.example.travelapp.repository;

import com.example.travelapp.model.Route;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query(value = "SELECT r.* FROM Routes r "
            + "JOIN route_places rp ON r.id = rp.route_id "
            + "GROUP BY r.id "
            + "HAVING COUNT(rp.place_id) >= :minPlaces", nativeQuery = true)
    List<Route> findRoutesWithMinimumPlaces(@Param("minPlaces") int minPlaces);

}