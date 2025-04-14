package com.example.travelapp.service;

import com.example.travelapp.exception.NotFoundException;
import com.example.travelapp.model.LogObj;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import org.springframework.cache.Cache;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class AsyncLogService {
    private static final String LOG_FILE_PATH = "app.log";

    @Async("taskExecutor")
    public void createLogs(Long taskId, String date, Cache logsCache) {
        try {
            Thread.sleep(5000);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate logDate = LocalDate.parse(date, formatter);

            Path path = Paths.get(LOG_FILE_PATH);
            List<String> logLines = Files.readAllLines(path);
            String formattedDate = logDate.format(formatter);
            List<String> currentLogs = logLines.stream()
                    .filter(line -> line.startsWith(formattedDate))
                    .toList();

            if (currentLogs.isEmpty()) {
                LogObj task = new LogObj(taskId, "FAILED");
                logsCache.put(taskId, task);
                throw new NotFoundException("No logs for date: " + date);
            }

            // Create temp file with or without POSIX permissions depending on OS support
            Path logFile;
            try {
                // Try with POSIX permissions first
                FileAttribute<Set<PosixFilePermission>> attr =
                        PosixFilePermissions.asFileAttribute(
                                PosixFilePermissions.fromString("rwx------"));
                logFile = Files.createTempFile("logs-" + formattedDate, ".log", attr);
            } catch (UnsupportedOperationException e) {
                // Fallback to simple file creation if POSIX not supported
                logFile = Files.createTempFile("logs-" + formattedDate, ".log");
            }

            Files.write(logFile, currentLogs);
            logFile.toFile().deleteOnExit();

            LogObj task = new LogObj(taskId, "COMPLETED");
            task.setFilePath(logFile.toString());
            logsCache.put(taskId, task);

        } catch (IOException e) {
            LogObj task = new LogObj(taskId, "FAILED");
            task.setErrorMessage(e.getMessage());
            logsCache.put(taskId, task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}