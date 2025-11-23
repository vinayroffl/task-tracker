package com.vinay.tasktracker;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

  private Long id;
  private String description;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Task(Long id, String description) {
    this.id = id;
    this.description = description;
    this.status = "todo";
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  Task(
      Long id,
      String description,
      String status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super();
    this.id = id;
    this.description = description;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Task() {
    this.status = "todo";
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  public Long getId() {
    return this.id;
  }

  public String getDescription() {
    return this.description;
  }

  public String getStatus() {
    return this.status;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setStatus(String status) {
    this.status = status;
    this.updatedAt = LocalDateTime.now();
  }

  public void setDescription(String description) {
    this.description = description;
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdAt, description, id, status, updatedAt);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Task)) {
      return false;
    }
    Task other = (Task) obj;
    return Objects.equals(description, other.description) && Objects.equals(id, other.id);
  }

  @Override
  public String toString() {
    return "Task [id="
        + id
        + ", description="
        + description
        + ", status="
        + status
        + ", createdAt="
        + createdAt
        + ", updatedAt="
        + updatedAt
        + "]";
  }
}
