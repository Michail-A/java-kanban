package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
        gson = getGson();
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        sendText(h, gson.toJson(manager.getPrioritizedTasks()));
    }
}
