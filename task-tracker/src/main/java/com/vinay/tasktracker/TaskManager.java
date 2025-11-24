package com.vinay.tasktracker;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TaskManager {

  private List<Task> tasks = new ArrayList<>();
  FileManager fileManager = new FileManager(tasks);

  public String addTask(String description) {
    Task task = new Task(fileManager.lastId + 1, description);
    if (!tasks.contains(task)) {
      tasks.add(task);
      fileManager.lastId = tasks.get(tasks.size() - 1).getId();
      fileManager.saveTaskDetailsToFile(tasks);
      return String.format("Task added successfully (ID: %d)", fileManager.lastId);
    }
    return "Task already exists with description: " + description;
  }

  public String updateTaskDescription(Long id, String description) {
    try {
      Task existingTask = findById(id).get();
      existingTask.setDescription(description);
      fileManager.saveTaskDetailsToFile(tasks);
    } catch (NoSuchElementException e) {
      return String.format("No task found with ID: %d", id);
    }
    return String.format("Task updated successfully (ID: %d)", id);
  }

  public String deleteTask(Long id) {
    try {
      Task existingTask = findById(id).get();
      tasks.remove(tasks.indexOf(existingTask));
      fileManager.saveTaskDetailsToFile(tasks);
    } catch (NoSuchElementException e) {
      return String.format("No task found with ID: %d", id);
    }
    return String.format("Task deleted successfully (ID: %d)", id);
  }

  public String updateTaskStatus(Long id, String status) {
    try {
      Task existingTask = findById(id).get();
      existingTask.setStatus(status);
      fileManager.saveTaskDetailsToFile(tasks);
    } catch (NoSuchElementException e) {
      return String.format("No task found with ID: %d", id);
    }
    return String.format("Task marked as %s", status);
  }

  public String listAllTasks() {
    return listTasks(tasks);
  }

  public String listTasksByStatus(String status) {
    return listTasks(tasks.stream().filter(t -> t.getStatus().equals(status)).toList());
  }

  public Optional<Task> findById(long id) {
    return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
  }

  private String listTasks(List<Task> tasks) {
    if (tasks.isEmpty()) {
      return "No tasks are listed";
    }
    StringBuilder sb = new StringBuilder();
    int idLen = 0;
    int descLen = 0;
    for (Task task : tasks) {
      idLen = task.getId().toString().length() > idLen ? task.getId().toString().length() : idLen;
      descLen = task.getDescription().length() > descLen ? task.getDescription().length() : descLen;
      sb.append("- ")
          .append(task.getDescription())
          .append(" ".repeat(2))
          .append(task.getStatus())
          .append(" ".repeat(2))
          .append("(ID: ")
          .append(task.getId())
          .append(")")
          .append("\n");
    }
    return sb.toString();
  }
}
