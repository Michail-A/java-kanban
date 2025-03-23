package http;

import com.google.gson.Gson;
import http.handler.BaseHttpHandler;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
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

public class EpicHandlerTest {
    static String BASE_URL = "http://localhost:8080/epics";
    TaskManager manager;
    HttpTaskServer server;
    HttpClient client;
    Gson gson;
    URI url;

    SubTask subTask;
    Epic epic;

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        client = HttpClient.newHttpClient();
        gson = BaseHttpHandler.getGson();
        url = URI.create(BASE_URL);

        epic = new Epic("Epic", "Epic");
        subTask = new SubTask("Subtask", "subtask", 1, Duration.ofMinutes(15L),
                LocalDateTime.now());
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        manager.addSubTask(subTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getEpics()), response.body());
    }

    @Test
    void getEpicForId() throws IOException, InterruptedException {
        manager.addEpic(epic);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/" + epic.getId())).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(epic), response.body());
    }

    @Test
    void getSubTasksByEpic() throws IOException, InterruptedException {
        manager.addEpic(epic);
        subTask.setEpicId(epic.getId());
        manager.addSubTask(subTask);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/" + epic.getId()
                + "/subtasks")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getSubTasksByEpicId(epic.getId())), response.body());
    }

    @Test
    void addEpic() throws IOException, InterruptedException {

        String epicJson = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(manager.getEpics().size(), 1);

    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        manager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + epic.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpics().size(), 0);
    }
}
