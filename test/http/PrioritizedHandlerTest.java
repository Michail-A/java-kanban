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

public class PrioritizedHandlerTest {

    static String BASE_URL = "http://localhost:8080/prioritized";
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
        manager.addTask(task1);
        manager.addTask(task2);
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getPrioritized() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(manager.getPrioritizedTasks()), response.body());
    }
}
