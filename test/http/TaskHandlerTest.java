package http;

import com.google.gson.Gson;
import http.handler.BaseHttpHandler;
import manager.Managers;
import manager.TaskManager;
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

public class TaskHandlerTest {
    static String BASE_URL = "http://localhost:8080/tasks";
    TaskManager manager;
    HttpTaskServer server;
    HttpClient client;
    Gson gson;
    URI url;

    Task task1;
    Task task2;

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        client = HttpClient.newHttpClient();
        gson = BaseHttpHandler.getGson();
        url = URI.create(BASE_URL);

        task1 = new Task("Current", "Current", Duration.ofMinutes(30L), LocalDateTime.now());
        task2 = new Task("NewTask", "NewTask", Duration.ofMinutes(30L),
                task1.getStartTime().minus(Duration.ofMinutes(30L)));
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String actualTasks = gson.toJson(manager.getTasks());
        assertEquals(response.body(), actualTasks);
    }

    @Test
    void getTasksForId() throws IOException, InterruptedException {
        manager.addTask(task1);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/1")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.fromJson(response.body(), Task.class), task1);
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(manager.getTasks().size(), 1);
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        manager.addTask(task1);
        Task newTask = task1;
        newTask.setTitle("new new new");

        String taskJson = gson.toJson(newTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(manager.getTaskById(task1.getId()), newTask);
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        manager.addTask(task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + task1.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getTasks().size(), 0);
    }
}
