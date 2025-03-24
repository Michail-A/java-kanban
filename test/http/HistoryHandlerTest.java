package http;

import com.google.gson.Gson;
import http.handler.BaseHttpHandler;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryHandlerTest {
    static String BASE_URL = "http://localhost:8080/history";
    TaskManager manager;
    HttpTaskServer server;
    HttpClient client;
    Gson gson;
    URI url;

    SubTask subTask;
    Epic epic;
    Task task;

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        client = HttpClient.newHttpClient();
        gson = BaseHttpHandler.getGson();
        url = URI.create(BASE_URL);

        task = new Task("Task", "Task");
        epic = new Epic("Epic", "Epic");
        manager.addTask(task);
        manager.addEpic(epic);
        subTask = new SubTask("Subtask", "subtask", epic.getId(), Duration.ofMinutes(15L),
                LocalDateTime.now());
        manager.addSubTask(subTask);
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubTaskById(subTask.getId());
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getHistory()), response.body());
    }
}
