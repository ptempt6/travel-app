package com.example.travelapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devast")
public class DevastController {
    @PostMapping
    ResponseEntity<Integer> doHook(@RequestBody String body) {
        System.out.println(body);
        return null;
    }
}
