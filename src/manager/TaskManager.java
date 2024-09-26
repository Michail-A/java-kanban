package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subTask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTasksById(int id);

    List<SubTask> getSubTasksByEpicId(int id);

}
