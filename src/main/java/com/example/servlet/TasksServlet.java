package com.example.servlet;

import com.example.dao.TaskDAO;
import com.example.model.Task;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet for handling CRUD operations on tasks. Endpoints: - GET /api/tasks - Get all tasks - GET
 * /api/tasks/{id} - Get task by id - POST /api/tasks - Create new task - PUT /api/tasks/{id} -
 * Update task - DELETE /api/tasks/{id} - Delete task
 */
@WebServlet("/api/tasks/*")
public class TasksServlet extends HttpServlet {
  private Gson gson = new Gson();

  @Override
  public void init() throws ServletException {
    super.init();
    // Initialize database on servlet startup
    TaskDAO.initialize();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    PrintWriter out = resp.getWriter();

    String pathInfo = req.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
        // GET /api/tasks - Get all tasks
        List<Task> tasks = TaskDAO.readAll();
        out.print(gson.toJson(tasks));
      } else {
        // GET /api/tasks/{id} - Get task by id
        int id = Integer.parseInt(pathInfo.substring(1));
        Task task = TaskDAO.read(id);
        if (task != null) {
          out.print(gson.toJson(task));
          resp.setStatus(HttpServletResponse.SC_OK);
        } else {
          resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
          out.print(gson.toJson(new ErrorResponse("Task not found")));
        }
      }
    } catch (NumberFormatException e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print(gson.toJson(new ErrorResponse("Invalid task id")));
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    PrintWriter out = resp.getWriter();

    try {
      // Parse JSON request body
      Task task = gson.fromJson(req.getReader(), Task.class);

      if (task == null || task.getTitle() == null || task.getTitle().isEmpty()) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print(gson.toJson(new ErrorResponse("Title is required")));
        return;
      }

      // Create task
      Task createdTask = TaskDAO.create(task);
      resp.setStatus(HttpServletResponse.SC_CREATED);
      out.print(gson.toJson(createdTask));
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print(gson.toJson(new ErrorResponse("Invalid request: " + e.getMessage())));
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    PrintWriter out = resp.getWriter();

    String pathInfo = req.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print(gson.toJson(new ErrorResponse("Task id is required")));
        return;
      }

      int id = Integer.parseInt(pathInfo.substring(1));
      Task task = gson.fromJson(req.getReader(), Task.class);
      task.setId(id);

      if (TaskDAO.update(task)) {
        resp.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(task));
      } else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        out.print(gson.toJson(new ErrorResponse("Task not found")));
      }
    } catch (NumberFormatException e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print(gson.toJson(new ErrorResponse("Invalid task id")));
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print(gson.toJson(new ErrorResponse("Invalid request: " + e.getMessage())));
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    PrintWriter out = resp.getWriter();

    String pathInfo = req.getPathInfo();

    try {
      if (pathInfo == null || pathInfo.equals("/")) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print(gson.toJson(new ErrorResponse("Task id is required")));
        return;
      }

      int id = Integer.parseInt(pathInfo.substring(1));

      if (TaskDAO.delete(id)) {
        resp.setStatus(HttpServletResponse.SC_OK);
        out.print(gson.toJson(new SuccessResponse("Task deleted successfully")));
      } else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        out.print(gson.toJson(new ErrorResponse("Task not found")));
      }
    } catch (NumberFormatException e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print(gson.toJson(new ErrorResponse("Invalid task id")));
    }
  }

  /** Helper class for error responses. */
  private static class ErrorResponse {
    String error;

    ErrorResponse(String error) {
      this.error = error;
    }
  }

  /** Helper class for success responses. */
  private static class SuccessResponse {
    String message;

    SuccessResponse(String message) {
      this.message = message;
    }
  }
}
