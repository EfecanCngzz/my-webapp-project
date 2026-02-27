package com.example.model;

/** Task model class representing a TODO item. */
public class Task {
  private int id;
  private String title;
  private boolean completed;

  /** Default constructor. */
  public Task() {}

  /** Constructor with parameters. */
  public Task(int id, String title, boolean completed) {
    this.id = id;
    this.title = title;
    this.completed = completed;
  }

  /** Constructor without id (for creation). */
  public Task(String title, boolean completed) {
    this.title = title;
    this.completed = completed;
  }

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  @Override
  public String toString() {
    return "Task{" + "id=" + id + ", title='" + title + '\'' + ", completed=" + completed + '}';
  }
}
