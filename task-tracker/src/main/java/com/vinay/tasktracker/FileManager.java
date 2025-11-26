package com.vinay.tasktracker;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

public class FileManager {

  private final Path filePath = Paths.get("data.json");
  long lastId = 0;

  public FileManager(List<Task> tasks) {
    loadTaskDetails(tasks);
  }

  private void loadTaskDetails(List<Task> tasks) {
    try {
      Files.createFile(filePath);
      String jsonSkelton = createJsonSkelton();
      Files.write(filePath, jsonSkelton.getBytes());
    } catch (FileAlreadyExistsException e) {
      String jsonString = readFile(filePath);
      jsonToTaskDetails(jsonString, tasks);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void jsonToTaskDetails(String jsonString, List<Task> tasks) {
    int lastIdValueStart = jsonString.indexOf("\"lastId\":") + "\"lastId\":".length() + 2;
    int lastIdValueEnd = jsonString.indexOf(",\n", lastIdValueStart) - 1;
    lastId = Long.parseLong(jsonString.substring(lastIdValueStart, lastIdValueEnd));
    int tasksValueStart = jsonString.indexOf("\"tasks\"") + 10;
    int tasksValueEnd = jsonString.lastIndexOf("]");
    String taskValuesString = jsonString.substring(tasksValueStart, tasksValueEnd);
    int indexOfTaskIdValueStart = taskValuesString.indexOf("\"id\":") + "\"id\":".length() + 2;
    int indexOfTaskDescriptionValueStart = taskValuesString.indexOf("\"description\":") + "\"description\":".length()
        + 2;
    int indexOfTaskStatusValueStart = taskValuesString.indexOf("\"status\":") + "\"status\":".length() + 2;
    int indexOfTaskCreatedAtValueStart = taskValuesString.indexOf("\"createdAt\":") + "\"createdAt\":".length() + 2;
    int indexOfTaskUpdatedAtValueStart = taskValuesString.indexOf("\"updatedAt\":") + "\"updatedAt\":".length() + 2;

    int lastIndexOfIdValue = taskValuesString.lastIndexOf("\"id\":") + "\"id\":".length() + 2;

    while (indexOfTaskIdValueStart > 0) {

      int indexOfTaskIdValueEnd = taskValuesString.indexOf(",\n", indexOfTaskIdValueStart) - 1;
      Long id = Long.parseLong(taskValuesString.substring(indexOfTaskIdValueStart, indexOfTaskIdValueEnd));
      indexOfTaskIdValueStart = taskValuesString.indexOf("\"id\":", indexOfTaskIdValueEnd) + "\"id\":".length() + 2;

      int indexOfTaskDescriptionValueEnd = taskValuesString.indexOf(",\n", indexOfTaskDescriptionValueStart) - 1;
      String description = taskValuesString.substring(indexOfTaskDescriptionValueStart, indexOfTaskDescriptionValueEnd);
      indexOfTaskDescriptionValueStart = taskValuesString.indexOf("\"description\":", indexOfTaskDescriptionValueEnd)
          + "\"description\":".length()
          + 2;

      int indexOfTaskStatusValueEnd = taskValuesString.indexOf(",\n", indexOfTaskStatusValueStart) - 1;
      String status = taskValuesString.substring(indexOfTaskStatusValueStart, indexOfTaskStatusValueEnd);
      indexOfTaskStatusValueStart = taskValuesString.indexOf("\"status\":", indexOfTaskStatusValueEnd)
          + "\"status\":".length()
          + 2;

      int indexOfTaskCreatedAtValueEnd = taskValuesString.indexOf(",\n", indexOfTaskCreatedAtValueStart) - 1;
      LocalDateTime createdAt = LocalDateTime.parse(
          taskValuesString.substring(
              indexOfTaskCreatedAtValueStart, indexOfTaskCreatedAtValueEnd));
      indexOfTaskCreatedAtValueStart = taskValuesString.indexOf("\"createdAt\":", indexOfTaskCreatedAtValueEnd)
          + "\"createdAt\":".length()
          + 2;

      int indexOfTaskUpdatedAtValueEnd = taskValuesString.indexOf("\n", indexOfTaskUpdatedAtValueStart) - 2;
      LocalDateTime updatedAt = LocalDateTime.parse(
          taskValuesString.substring(
              indexOfTaskUpdatedAtValueStart, indexOfTaskUpdatedAtValueEnd));
      indexOfTaskUpdatedAtValueStart = taskValuesString.indexOf("\"updatedAt\":", indexOfTaskUpdatedAtValueEnd)
          + "\"updatedAt\":".length()
          + 2;

      Task task = new Task(id, description, status, createdAt, updatedAt);
      if (!tasks.contains(task))
        tasks.add(task);
      // Check if we've processed all tasks by looking ahead
      if (taskValuesString.indexOf("\"id\":", indexOfTaskIdValueEnd) == -1) {
        break;
      }
    }
  }

  private String createJsonSkelton() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n")
        .append(" ".repeat(4))
        .append("\"")
        .append("lastId")
        .append("\": \"")
        .append(0)
        .append("\",\n")
        .append(" ".repeat(4))
        .append("\"")
        .append("tasks")
        .append("\": ")
        .append("[]\n")
        .append("}\n");
    return sb.toString();
  }

  private String readFile(Path filePath2) {
    byte[] fileBytes = null;
    try {
      fileBytes = Files.readAllBytes(filePath);
    } catch (IOException e) {
      System.out.println("Exception while reading file" + e.getMessage());
    }
    return new String(fileBytes);
  }

  public void saveTaskDetailsToFile(List<Task> tasks) {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n")
        .append("    ")
        .append("\"")
        .append("lastId")
        .append("\": \"")
        .append(lastId)
        .append("\",\n")
        .append("    ")
        .append("\"")
        .append("tasks")
        .append("\": ")
        .append("[");
    List<String> taskStringsList = tasks.stream().map(task -> taskToJson(task)).toList();
    if (taskStringsList.size() > 0) {
      String taskStringsListValues = String.join(", ", taskStringsList);
      int ignoreFirstline = 1;
      for (Iterator<String> iterator = taskStringsListValues.lines().iterator(); iterator.hasNext();) {
        if (ignoreFirstline == 1) {
          sb.append(iterator.next()).append("\n");
        } else {
          sb.append("        ").append(iterator.next()).append("\n");
        }
        ignoreFirstline += 1;
      }
      sb.append("    ").append("]\n");
    } else {
      sb.append("]\n");
    }
    sb.append("}\n");

    writeFile(sb.toString());
  }

  private void writeFile(String string) {
    try {
      Files.writeString(filePath, string);
    } catch (IOException e) {
      System.out.println("Failed  to write to file");
    }
  }

  private String taskToJson(Task task) {
    StringBuilder taskInJson = new StringBuilder();
    taskInJson
        .append("{\n")
        .append("    ")
        .append("\"id\": \"")
        .append(task.getId())
        .append("\",\n")
        .append("    ")
        .append("\"description\": \"")
        .append(task.getDescription())
        .append("\",\n")
        .append("    ")
        .append("\"status\": \"")
        .append(task.getStatus())
        .append("\",\n")
        .append("    ")
        .append("\"createdAt\": \"")
        .append(task.getCreatedAt())
        .append("\",\n")
        .append("    ")
        .append("\"updatedAt\": \"")
        .append(task.getUpdatedAt())
        .append("\"\n")
        .append("}");
    return taskInJson.toString();
  }
}
