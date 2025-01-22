package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class FileBackedTaskManagerTest {

    Task testTask;
    Epic testEpic;
    SubTask testSubTask;
    Path data;
    Path dataHistory;
    HistoryManager historyManager;
    FileBackedTaskManager testManager;
    FileBackedTaskManager equalManager;

    @BeforeEach
    void setUp() throws IOException {
        data = Files.createTempFile("test_data", "csv");
        dataHistory = Files.createTempFile("test_data_history", "csv");
        historyManager = new InMemoryHistoryManager();
        testManager = new FileBackedTaskManager(historyManager, data, dataHistory);
        testTask = new Task("task", "task");
        testManager.addTask(testTask);
        testEpic = new Epic("epic", "epic");
        testManager.addEpic(testEpic);
        testSubTask = new SubTask("subtask", "subtask", testEpic.getId());
        testManager.addSubTask(testSubTask);
        testManager.getTaskById(testTask.getId());
        testManager.getEpicById(testEpic.getId());
        testManager.getSubTaskById(testSubTask.getId());
    }

    @Test
    void loadFromFile() {
        equalManager = FileBackedTaskManager.loadFromFile(data, dataHistory);

        assertIterableEquals(equalManager.getTasks(), testManager.getTasks());
        assertIterableEquals(equalManager.getEpics(), testManager.getEpics());
        assertIterableEquals(equalManager.getSubTasks(), testManager.getSubTasks());
        assertIterableEquals(equalManager.getHistoryIds(), testManager.getHistoryIds());
        assertEquals(equalManager.getId(), testManager.getId());
    }
}