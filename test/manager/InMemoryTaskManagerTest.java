package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager manager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    SubTask subTask1;
    SubTask subTask2;

    @BeforeEach
    void BeforeEach() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        task1 = new Task("test", "test");
        task2 = new Task("test2", "test2");
        epic1 = new Epic("test", "test");
        epic2 = new Epic("test2", "test2");
        subTask1 = new SubTask("test", "test", 0);
        subTask2 = new SubTask("test2", "test2", 0);
    }

    @Test
    void getTasks() {
        manager.addTask(task1);
        manager.addTask(task2);

        assertEquals(2, manager.getTasks().size());
    }

    @Test
    void getEpics() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        assertEquals(2, manager.getEpics().size());
    }

    @Test
    void getSubTasks() {
        manager.addEpic(epic1);
        subTask1 = new SubTask("test", "test", epic1.getId());
        subTask2 = new SubTask("test2", "test2", epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        assertEquals(2, manager.getSubTasks().size());
    }

    @Test
    void deleteTasks() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.deleteTasks();

        assertEquals(0, manager.getTasks().size());
    }

    @Test
    void deleteEpics() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.deleteEpics();

        assertEquals(0, manager.getEpics().size());
    }

    @Test
    void deleteSubTasks() {
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        manager.deleteSubTasks();
        assertEquals(0, manager.getSubTasks().size());
    }

    @Test
    void getTaskById() {
        manager.addTask(task1);
        Task task = manager.getTaskById(task1.getId());

        assertEquals(task1.getDescription(), task.getDescription());
        assertEquals(task1.getTitle(), task.getTitle());
    }

    @Test
    void getEpicById() {
        manager.addEpic(epic1);
        Epic epic = manager.getEpicById(epic1.getId());

        assertEquals(epic1.getTitle(), epic.getTitle());
        assertEquals(epic1.getDescription(), epic.getDescription());
    }

    @Test
    void getSubTaskById() {
        manager.addEpic(epic1);
        subTask1 = new SubTask("test", "test", epic1.getId());
        manager.addSubTask(subTask1);
        SubTask subTask = manager.getSubTaskById(subTask1.getId());

        assertEquals(subTask1.getTitle(), subTask.getTitle());
        assertEquals(subTask1.getDescription(), subTask.getDescription());
    }

    @Test
    void updateTask() {
        manager.addTask(task1);
        Task updatedTask = new Task("update", "update", task1.getId(), Status.IN_PROGRESS);
        manager.updateTask(updatedTask);
        Task task = manager.getTaskById(task1.getId());

        assertEquals(updatedTask.getTitle(), task.getTitle());
        assertEquals(updatedTask.getDescription(), task.getDescription());
        assertEquals(updatedTask.getStatus(), task.getStatus());
    }

    @Test
    void updateEpic() {
        manager.addEpic(epic1);
        Epic updatedEpic = new Epic(epic1.getId(), "update", "update", null);
        manager.updateEpic(updatedEpic);
        Epic epic = manager.getEpicById(epic1.getId());

        assertEquals(updatedEpic.getTitle(), epic.getTitle());
        assertEquals(updatedEpic.getDescription(), epic.getDescription());
        assertNotNull(epic.getIdSubTasks());
    }

    @Test
    void updateSubtask() {
        manager.addEpic(epic1);
        subTask1 = new SubTask("test", "test", epic1.getId());
        subTask2 = new SubTask("test2", "test2", epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        SubTask updatedSubTask = new SubTask("update", "update", subTask1.getId(), Status.IN_PROGRESS,
                epic1.getId());
        manager.updateSubtask(updatedSubTask);

        SubTask subTask = manager.getSubTaskById(subTask1.getId());

        assertEquals(updatedSubTask.getDescription(), subTask.getDescription());
        assertEquals(updatedSubTask.getTitle(), subTask.getTitle());
        assertEquals(updatedSubTask.getStatus(), subTask.getStatus());
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic1.getId()).getStatus());

        updatedSubTask = new SubTask("update", "update", subTask1.getId(), Status.DONE, epic1.getId());
        SubTask updatedSubTask2 = new SubTask("update", "update", subTask2.getId(), Status.DONE,
                epic1.getId());
        manager.updateSubtask(updatedSubTask);
        manager.updateSubtask(updatedSubTask2);

        assertEquals(Status.DONE, manager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    void deleteTaskById() {
        manager.addTask(task1);
        manager.deleteTaskById(task1.getId());

        assertNull(manager.getTaskById(task1.getId()));
    }

    @Test
    void deleteEpicById() {
        manager.addEpic(epic1);
        subTask1 = new SubTask("test", "test", epic1.getId());
        subTask2 = new SubTask("test2", "test2", epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.deleteEpicById(epic1.getId());

        assertNull(manager.getEpicById(epic1.getId()));

    }

    @Test
    void deleteSubTasksById() {
        manager.addEpic(epic1);
        subTask1 = new SubTask("test", "test", epic1.getId());
        subTask2 = new SubTask("test2", "test2", epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        SubTask updatedSubTask = new SubTask("update", "update", subTask1.getId(), Status.IN_PROGRESS,
                epic1.getId());
        manager.updateSubtask(updatedSubTask);

        manager.deleteSubTasksById(updatedSubTask.getId());

        assertEquals(1, manager.getSubTasksByEpicId(updatedSubTask.getEpicId()).size());
        assertEquals(Status.NEW, manager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    void getSubTasksByEpicId() {
        manager.addEpic(epic1);
        subTask1 = new SubTask("test", "test", epic1.getId());
        subTask2 = new SubTask("test2", "test2", epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        assertEquals(2, manager.getSubTasksByEpicId(epic1.getId()).size());
    }
}