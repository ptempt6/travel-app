package com.example.travelapp.service;


import com.example.travelapp.exception.InternalServerErrorException;
import com.example.travelapp.exception.NotFoundException;
import com.example.travelapp.model.LogObj;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final AsyncLogService asyncLogService;
    private final CacheManager cacheManager;
    private final AtomicLong idCounter = new AtomicLong(1);

    public LogService(AsyncLogService asyncLogService, CacheManager cacheManager) {
        this.asyncLogService = asyncLogService;
        this.cacheManager = cacheManager;
    }

    public Long startLogCreation(String date) {
        Long id = idCounter.getAndIncrement();
        LogObj task = new LogObj(id, "IN_PROGRESS");
        Cache logsCache = cacheManager.getCache("logTasks");
        if (logsCache != null) {
            logsCache.put(id, task);
        }
        asyncLogService.createLogs(id, date, logsCache);
        return id;
    }

    public LogObj getStatus(Long taskId) {
        Cache logsCache = cacheManager.getCache("logTasks");
        if (logsCache != null) {
            LogObj task = (LogObj) logsCache.get(taskId);
            if (task != null) {
                return task;
            }
            throw new NotFoundException("Task not found");
        } else {
            throw new InternalServerErrorException("error");
        }
    }

    public ResponseEntity<Resource> downloadCreatedLogs(Long taskId) throws IOException {
        LogObj task = getStatus(taskId);
        if (task == null) {
            throw new NotFoundException("Task not found");
        }
        if (!"COMPLETED".equals(task.getStatus())) {
            throw new BadRequestException("Logs not ready");
        }

        Path path = Paths.get(task.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}