package com.example.travelapp.controller;

import com.example.travelapp.service.CounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/visit")
@Tag(name = "Visit tracking", description = "Visit counter operations")
public class CounterController {

    private final CounterService counterService;


    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }


    @Operation(summary = "Register visit", description = "Increments visit counter")
    @GetMapping
    public void regVisit() {
        counterService.increment();
    }


    @Operation(summary = "Get visit count", description = "Returns visit amount")
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVisits", counterService.getTotalVisits());

        return ResponseEntity.ok(stats);
    }
}
