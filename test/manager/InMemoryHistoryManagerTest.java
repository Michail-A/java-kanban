package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    TaskManager taskManager;
    HistoryManager historyManager;
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        task = new Task("test", "test");
        epic = new Epic("test", "test");
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        subTask = new SubTask("test", "test", epic.getId());
        taskManager.addSubTask(subTask);
    }

    @Test
    void writeHistory() {
        taskManager.getTaskById(task.getId());

        assertEquals(1, historyManager.getHistory().size());

        taskManager.getEpicById(epic.getId());
        assertEquals(2, historyManager.getHistory().size());

        taskManager.getSubTaskById(subTask.getId());

        assertEquals(3, historyManager.getHistory().size());
    }

}