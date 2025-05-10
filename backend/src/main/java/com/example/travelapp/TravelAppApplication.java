package com.example.travelapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
        info = @Info(title = "Travel App API", version = "1.0",
                description = "API for Travel Application")
)
@SpringBootApplication
public class TravelAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelAppApplication.class, args);
    }

}
