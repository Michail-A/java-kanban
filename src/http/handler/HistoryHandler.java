package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
        gson = getGson();
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        List<Task> history = manager.getHistory();
        sendText(h, gson.toJson(history));
    }
}
