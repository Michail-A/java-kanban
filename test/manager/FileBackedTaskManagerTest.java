package manager;

import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class FileBackedTaskManagerTest extends TaskManagerTest {

    Path data;
    Path dataHistory;
    HistoryManager historyManager;
    FileBackedTaskManager equalManager;

    @Override
    void setManager() throws IOException {
        data = Files.createTempFile("test_data", "csv");
        dataHistory = Files.createTempFile("test_data_history", "csv");
        historyManager = new InMemoryHistoryManager();
        manager = new FileBackedTaskManager(historyManager, data, dataHistory);
    }

    @Test
    void loadFromFile() {
        manager.addTask(task1);
        manager.addEpic(epic1);
        subTask1.setEpicId(epic1.getId());
        manager.addSubTask(subTask1);

        equalManager = FileBackedTaskManager.loadFromFile(data, dataHistory);

        assertIterableEquals(equalManager.getTasks(), manager.getTasks());
        assertIterableEquals(equalManager.getEpics(), manager.getEpics());
        assertIterableEquals(equalManager.getSubTasks(), manager.getSubTasks());
        assertIterableEquals(equalManager.getHistory(), manager.getHistory());
    }

    @Test
    void loadFromFileWithData() {
        task1 = new Task("task", "task", Duration.ofMinutes(15L), LocalDateTime.now());
        manager.addTask(task1);
        manager.addEpic(epic1);
        subTask1 = new SubTask("subtask", "subtask", epic1.getId(), Duration.ofMinutes(15L),
                LocalDateTime.now().plusMinutes(30L));
        manager.addSubTask(subTask1);

        equalManager = FileBackedTaskManager.loadFromFile(data, dataHistory);

        assertIterableEquals(equalManager.getTasks(), manager.getTasks());
        assertIterableEquals(equalManager.getEpics(), manager.getEpics());
        assertIterableEquals(equalManager.getSubTasks(), manager.getSubTasks());
        assertIterableEquals(equalManager.getHistory(), manager.getHistory());
    }

}