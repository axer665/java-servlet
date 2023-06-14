package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing

      if (method.equals("GET")) {
        this.get(path, resp);
      } else if (method.equals("POST")) {
        this.post(path, req, resp);
      } else if (method.equals("DELETE")) {
        this.delete(path, req, resp);
      } else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  // метод, обрабатывающий GET-запросы
  private void get(String path, HttpServletResponse resp) throws IOException {
    if (path.equals("/api/posts/")) {
      controller.all(resp);
    } else if (path.matches("/api/posts/\\d+")) {
      final var id = Long.parseLong(path.substring(path.lastIndexOf("/")).substring(1));
      controller.getById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  // метод, обрабатывающий POST-запросы
  private void post(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (path.equals("/api/posts/")) {
      controller.save(req.getReader(), resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  // метод, обрабатывающий DELETE-запросы
  private void delete(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (path.matches("/api/posts/\\d+")) {
      final var id = Long.parseLong(path.substring(path.lastIndexOf("/")).substring(1));
      controller.removeById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}

