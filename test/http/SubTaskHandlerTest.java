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

public class SubTaskHandlerTest {
    static String BASE_URL = "http://localhost:8080/subtasks";
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
        manager.addEpic(epic);
        subTask = new SubTask("Subtask", "subtask", epic.getId(), Duration.ofMinutes(15L),
                LocalDateTime.now());
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getSubTasks() throws IOException, InterruptedException {
        manager.addSubTask(subTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubTasks().size(), 1);
    }

    @Test
    void getSubTasksForId() throws IOException, InterruptedException {
        manager.addSubTask(subTask);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/" + subTask.getId())).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(subTask), response.body());
    }

    @Test
    void addSubTask() throws IOException, InterruptedException {
        String subTaskJson = gson.toJson(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(manager.getSubTasks().size(), 1);
    }

    @Test
    void updateSubTask() throws IOException, InterruptedException {
        manager.addSubTask(subTask);
        SubTask newTask = subTask;
        newTask.setTitle("new new new");

        String taskJson = gson.toJson(newTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(manager.getSubTaskById(subTask.getId()), newTask);
    }

    @Test
    void deleteSubTask() throws IOException, InterruptedException {
        manager.addSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + subTask.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubTasks().size(), 0);
    }

}
