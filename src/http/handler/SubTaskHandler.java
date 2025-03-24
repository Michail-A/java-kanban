package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.EpicNotFoundException;
import manager.TaskManager;
import model.SubTask;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public SubTaskHandler(TaskManager manager) {
        this.manager = manager;
        gson = BaseHttpHandler.getGson();
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        Optional<Integer> idOpt = getIdFromPathWhereThirdPlace(h);
        int id = idOpt.orElse(-1);
        switch (method) {
            case "GET":
                if (id < 0) {
                    List<SubTask> subtasks = manager.getSubTasks();
                    String response = gson.toJson(subtasks);
                    sendText(h, response);
                } else {
                    SubTask subTask = manager.getSubTaskById(id);
                    if (subTask == null) {
                        sendNotFound(h);
                    } else {
                        sendText(h, gson.toJson(subTask));
                    }
                }
                break;

            case "POST":
                String body = readText(h);
                SubTask subTask = gson.fromJson(body, SubTask.class);
                try {
                    if (subTask.getId() > 0) {
                        manager.updateSubtask(subTask);
                        sendResponse(h, "Задача обновлена", 201);
                    } else {
                        manager.addSubTask(subTask);
                        sendResponse(h, "Задача добавлена", 201);
                    }
                } catch (IllegalArgumentException e) {
                    sendHasIntersection(h);
                } catch (EpicNotFoundException e) {
                    sendNotFound(h);
                }
                break;

            case "DELETE":
                manager.deleteSubTasksById(id);
                sendText(h, "Подзадача удалена");
                break;
        }
    }
}
