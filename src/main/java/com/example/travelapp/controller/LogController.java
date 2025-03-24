package com.example.travelapp.controller;

import com.example.travelapp.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/logs")
@Tag(name = "Log requests", description = "Operations with .log files")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @Operation(summary = "Get .log file",
            description = "Returns .log file with logs from specified date",
            responses = {
                @ApiResponse(responseCode = "200", description =
                            "Log was downloaded"),
                @ApiResponse(responseCode = "400", description =
                            "Invalid date format",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\":"
                                            +
                                    " \"Invalid date format. Required dd-mm-yyyy\" }"))),
                @ApiResponse(responseCode = "404", description =
                            "Logs not found",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Logs not found\" }"))),
                @ApiResponse(responseCode = "500", description =
                            "Internal server error",
                            content = @Content(schema = @Schema(example =
                                    "{ \"error\": \"Internal server error\" }")))})
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadLogs(
            @Parameter(description = "Date to get logs for", example = "19-03-2025")
            @RequestParam String date) {

        Resource resource = logService.downloadLogs(date);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}