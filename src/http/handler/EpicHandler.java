package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Epic;
import model.SubTask;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
        gson = BaseHttpHandler.getGson();
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        Optional<Integer> idOpt = getIdFromPathWhereThirdPlace(h);
        int id = idOpt.orElse(-1);
        String[] pathParts = h.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                if (id < 0) {
                    List<Epic> epics = manager.getEpics();
                    sendText(h, gson.toJson(epics));
                } else {
                    Epic epic = manager.getEpicById(id);
                    if (epic == null) {
                        sendNotFound(h);
                        return;
                    }
                    if (pathParts.length > 3) {
                        List<SubTask> subTasks = manager.getSubTasksByEpicId(id);
                        sendText(h, gson.toJson(subTasks));
                    } else {
                        sendText(h, gson.toJson(epic));
                    }
                }
                break;

            case "POST":
                String body = readText(h);
                Epic epic = gson.fromJson(body, Epic.class);
                manager.addEpic(epic);
                sendResponse(h, "Задача добавлена", 201);
                break;

            case "DELETE":
                manager.deleteEpicById(id);
                sendText(h, "Задача удалена");
                break;
        }

    }
}
