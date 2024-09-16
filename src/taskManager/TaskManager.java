package taskManager;

import taskModel.Epic;
import taskModel.SubTask;
import taskModel.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int id = 0;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubTasks(new ArrayList<>());
            epic.updateStatus();
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void addTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        id++;
    }

    public void addEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        id++;
    }

    public void addSubTask(SubTask subTask) {
        if (!(epics.containsKey(subTask.getEpicId()))) {
            System.out.println("Эпика с id=" + subTask.getEpicId() + " не существует");
            return;
        }
        subTask.setId(id);
        subTasks.put(id, subTask);
        id++;
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        epic.updateStatus();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(SubTask subTask) {
        SubTask oldSubTask = subTasks.get(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.updateSubTask(subTask, oldSubTask);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        ArrayList<SubTask> subTasksByEpic = epics.get(id).getSubTasks();
        epics.remove(id);
        for (SubTask subTask : subTasksByEpic) {
            subTasks.remove(subTask.getId());
        }
    }

    public void deleteSubTasksById(int id) {
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.deleteSubTask(subTasks.get(id));
        subTasks.remove(id);
    }

    public ArrayList<Task> getSubTasksByEpicId(int id) {
        return new ArrayList<>(epics.get(id).getSubTasks());
    }
}
