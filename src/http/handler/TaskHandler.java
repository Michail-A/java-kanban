package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = getGson();
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        Optional<Integer> idOpt = getIdFromPath(h);

        switch (method) {
            case "GET":
                if (idOpt.isEmpty()) {
                    List<Task> tasks = manager.getTasks();
                    String response = gson.toJson(tasks);
                    sendText(h, response);
                } else {
                    int id = idOpt.get();
                    Task task = manager.getTaskById(id);
                    if (task == null) {
                        sendNotFound(h);
                    } else {
                        sendText(h, gson.toJson(task));
                    }
                }
                break;

            case "POST":
                String body = readText(h);
                Task task = gson.fromJson(body, Task.class);
                try {
                    if (task.getId() > 0) {
                        manager.updateTask(task);
                        sendResponse(h, "Задача обновлена", 201);
                    } else {
                        manager.addTask(task);
                        sendResponse(h, "Задача добавлена", 201);
                    }
                } catch (IllegalArgumentException e) {
                    sendHasIntersection(h);
                }
                break;

            case "DELETE":
                manager.deleteTaskById(idOpt.get());
                sendText(h, "Задача удалена");
                break;
        }
    }
}
