package com.example.travelapp.controller;

import com.example.travelapp.model.dto.request.UserRequestDto;
import com.example.travelapp.model.dto.response.UserResponseDto;
import com.example.travelapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "List of users retrieved successfully"),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", "
                                            +
                                     "\"status\": 500, \"message\": \"Internal server error\","
                                            +
                                            " \"path\": \"/users\" }")))
            }
    )
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a user by their unique ID.",
            responses = {
                @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
                @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 404,"
                                            +
                                 " \"message\": \"User not found\", \"path\": \"/users/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 500, "
                                            +
                             "\"message\": \"Internal server error\", \"path\": \"/users/123\" }")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(
            summary = "Create a new user",
            description = "Create a new user with the provided data.",
            responses = {
                @ApiResponse(responseCode = "200", description = "User created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 400, "
                                            +
                                "\"message\": \"Invalid input data\", \"path\": \"/users\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 500, "
                                            +
                           "\"message\": \"Internal server error\", \"path\": \"/users\" }")))
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @Operation(
            summary = "Update user details",
            description = "Update an existing user's details by their ID.",
            responses = {
                @ApiResponse(responseCode = "200", description = "User updated successfully"),
                @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 404, "
                                            +
                              "\"message\": \"User not found\", \"path\": \"/users/123\" }"))),
                @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 400, "
                                            +
                         "\"message\": \"Invalid input data\", \"path\": \"/users/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 500,"
                                            +
                          " \"message\": \"Internal server error\", \"path\": \"/users/123\" }")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @RequestBody @Valid UserRequestDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @Operation(
            summary = "Delete user",
            description = "Delete an existing user by their ID.",
            responses = {
                @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 404, "
                                            +
                            "\"message\": \"User not found\", \"path\": \"/users/123\" }"))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"timestamp\": \"2025-03-24T12:00:00\", \"status\": 500,"
                                            +
                       " \"message\": \"Internal server error\", \"path\": \"/users/123\" }")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
