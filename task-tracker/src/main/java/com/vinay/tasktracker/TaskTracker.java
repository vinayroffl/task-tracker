package com.vinay.tasktracker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskTracker {
  private static final Set<String> ALLOWED_COMMANDS =
      new HashSet<>(List.of("list", "add", "update", "delete", "mark-in-progress", "mark-done"));
  private static final Set<String> STATUS_SET =
      new HashSet<>(List.of("done", "todo", "in-progress"));
  private static final String USAGE =
      "Usage: java -jar task-tracker.jar <command> <arg>\n commands can be "
          + ALLOWED_COMMANDS.toString();
  private static final TaskManager taskManager = new TaskManager();

  public static void main(String[] args) {
    if (args.length < 1
        || !ALLOWED_COMMANDS.contains(args[0])
        || (args.length == 1 && !args[0].equals("list"))) {
      System.out.println(USAGE);
      return;
    }

    switch (args[0]) {
      case "list":
        if (args.length == 1) {
          System.out.println(taskManager.listAllTasks());
        } else if (!STATUS_SET.contains(args[1])) {
          System.out.println(USAGE);
        } else {
          System.out.println(taskManager.listTasksByStatus(args[1]));
        }
        break;
      case "add":
        if (args[1].trim().isEmpty() || args[1].trim().length() < 3) {
          System.out.println("Task description cannot be empty or less than 3 letters");
          System.out.println("Usage: java -jar task-tracker.jar add \"Do something\"");
        } else if (args[1].matches("\"")) {
          System.out.println("Task description should be alphanumeric");
        } else {
          System.out.println(taskManager.addTask(args[1]));
        }
        break;
      case "update":
        if (args.length < 3
            || args[1].trim().isEmpty()
            || args[2].trim().isEmpty()
            || args[2].trim().length() < 3) {
          System.out.println(
              "Usage: java -jar task-tracker.jar update <id> \"Do something and another thing\"");
        } else {
          System.out.println(taskManager.updateTaskDescription(Long.parseLong(args[1]), args[2]));
        }
        break;
      case "delete":
        if (args.length < 2 || args[1].trim().isEmpty()) {
          System.out.println("Usage: java -jar task-tracker.jar delete <id>");
        } else {
          System.out.println(taskManager.deleteTask(Long.parseLong(args[1])));
        }
        break;
      case "mark-in-progress":
        if (args.length < 2 || args[1].trim().isEmpty()) {
          System.out.println("Usage: java -jar task-tracker.jar mark-in-progress <id>");
        } else {
          System.out.println(taskManager.updateTaskStatus(Long.parseLong(args[1]), args[0]));
        }
        break;
      case "mark-done":
        if (args.length < 2 || args[1].trim().isEmpty()) {
          System.out.println("Usage: java -jar task-tracker.jar mark-in-progress <id>");
        } else {
          System.out.println(taskManager.updateTaskStatus(Long.parseLong(args[1]), "done"));
        }
        break;
      default:
        break;
    }
  }
}
