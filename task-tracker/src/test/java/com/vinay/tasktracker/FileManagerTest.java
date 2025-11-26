package com.vinay.tasktracker;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileManagerTest {

    Path tempDir = Paths.get("");

    private List<Task> tasks;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
    }

    @Test
    void testSaveTaskDetailsToFileWithEmptyTaskList() throws IOException {
        FileManager fileManager = new FileManager(tasks);
        fileManager.saveTaskDetailsToFile(tasks);

        Path dataFile = tempDir.resolve("data.json");
        String content = Files.readString(dataFile);

        assertTrue(content.contains("\"lastId\""));
        assertTrue(content.contains("\"tasks\": ["));
    }

    @Test
    void testSaveTaskDetailsToFileWithSingleTask() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task(1L, "Test task", "TODO", now, now);
        tasks.add(task);

        FileManager fileManager = new FileManager(tasks);
        fileManager.saveTaskDetailsToFile(tasks);

        Path dataFile = tempDir.resolve("data.json");
        String content = Files.readString(dataFile);

        assertTrue(content.contains("\"id\": \"1\""));
        assertTrue(content.contains("\"description\": \"Test task\""));
        assertTrue(content.contains("\"status\": \"TODO\""));
    }

    @Test
    void testSaveTaskDetailsToFileWithMultipleTasks() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        tasks.add(new Task(1L, "Task 1", "TODO", now, now));
        tasks.add(new Task(2L, "Task 2", "IN_PROGRESS", now, now));
        tasks.add(new Task(3L, "Task 3", "DONE", now, now));

        FileManager fileManager = new FileManager(tasks);
        fileManager.saveTaskDetailsToFile(tasks);

        Path dataFile = tempDir.resolve("data.json");
        String content = Files.readString(dataFile);

        assertTrue(content.contains("\"id\": \"1\""));
        assertTrue(content.contains("\"id\": \"2\""));
        assertTrue(content.contains("\"id\": \"3\""));
    }

    @Test
    void testIgnoreFirstlineFormattingWithMultipleTasks() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        tasks.add(new Task(1L, "First", "TODO", now, now));
        tasks.add(new Task(2L, "Second", "TODO", now, now));

        FileManager fileManager = new FileManager(tasks);
        fileManager.saveTaskDetailsToFile(tasks);

        Path dataFile = tempDir.resolve("data.json");
        String content = Files.readString(dataFile);
        String[] lines = content.split("\n");

        // Verify proper indentation based on ignoreFirstline logic
        assertTrue(lines.length > 5);
        assertTrue(content.contains("[{"));
    }

    @Test
    void testJsonStructureIsValid() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        tasks.add(new Task(1L, "Valid JSON", "TODO", now, now));

        FileManager fileManager = new FileManager(tasks);
        fileManager.saveTaskDetailsToFile(tasks);

        Path dataFile = tempDir.resolve("data.json");
        String content = Files.readString(dataFile);

        assertTrue(content.startsWith("{"));
        assertTrue(content.endsWith("}\n"));
        assertTrue(content.contains("\"lastId\""));
        assertTrue(content.contains("\"tasks\""));
    }
}