package com.example.dao;

import com.example.model.Task;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Data Access Object for Task CRUD operations. Uses SQLite database with jdbc:sqlite:todo.db. */
public class TaskDAO {
  private static final String DB_URL =
      "jdbc:sqlite:C:\\Users\\efeca\\OneDrive\\Masaüstü\\se3318\\repo1\\todo.db";
  private static final String DRIVER = "org.sqlite.JDBC";

  /** Initialize database and create table if not exists. */
  public static void initialize() {
    try {
      Class.forName(DRIVER);
      try (Connection conn = DriverManager.getConnection(DB_URL);
          Statement stmt = conn.createStatement()) {
        String sql =
            "CREATE TABLE IF NOT EXISTS tasks ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT NOT NULL,"
                + "completed BOOLEAN DEFAULT 0)";
        stmt.execute(sql);
      }
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  /** Create a new task. */
  public static Task create(Task task) {
    String sql = "INSERT INTO tasks (title, completed) VALUES (?, ?)";
    try (Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      pstmt.setString(1, task.getTitle());
      pstmt.setBoolean(2, task.isCompleted());
      pstmt.executeUpdate();

      try (ResultSet rs = pstmt.getGeneratedKeys()) {
        if (rs.next()) {
          task.setId(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return task;
  }

  /** Read all tasks. */
  public static List<Task> readAll() {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks";
    try (Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        Task task = new Task(rs.getInt("id"), rs.getString("title"), rs.getBoolean("completed"));
        tasks.add(task);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tasks;
  }

  /** Read task by id. */
  public static Task read(int id) {
    String sql = "SELECT * FROM tasks WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return new Task(rs.getInt("id"), rs.getString("title"), rs.getBoolean("completed"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** Update an existing task. */
  public static boolean update(Task task) {
    String sql = "UPDATE tasks SET title = ?, completed = ? WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, task.getTitle());
      pstmt.setBoolean(2, task.isCompleted());
      pstmt.setInt(3, task.getId());
      return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /** Delete a task by id. */
  public static boolean delete(int id) {
    String sql = "DELETE FROM tasks WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
