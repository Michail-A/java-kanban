package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private int id = 0;

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getSubTasks() {
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
            epic.removeAllSubTasks();
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
        updateEpicStatus(epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic updatedEpic = epics.get(epic.getId());
        updatedEpic.setTitle(epic.getTitle());
        updatedEpic.setDescription(epic.getDescription());
        updateEpicStatus(epic);
    }

    public void updateSubtask(SubTask subTask) {
        SubTask oldSubTask = subTasks.get(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.updateSubTask(subTask, oldSubTask);
        updateEpicStatus(epic);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        List<SubTask> subTasksByEpic = getSubTasksByEpicId(id);
        epics.remove(id);
        for (SubTask subTask : subTasksByEpic) {
            subTasks.remove(subTask.getId());
        }
    }

    public void deleteSubTasksById(int id) {
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.deleteSubTask(subTasks.get(id));
        subTasks.remove(id);
        updateEpicStatus(epic);
    }

    public List<SubTask> getSubTasksByEpicId(int id) {
        List<SubTask> subTasksByEpic = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == id) {
                subTasksByEpic.add(subTask);
            }
        }
        return subTasksByEpic;
    }

    private void updateEpicStatus(Epic epic) {
        int countDoneSubTasks = 0;
        int countNewSubTasks = 0;
        for (Integer idSubTask : epic.getIdSubTasks()) {
            SubTask subTask = subTasks.get(idSubTask);
            if (subTask.getStatus().equals(Status.DONE)) {
                countDoneSubTasks++;
            }
            if (subTask.getStatus().equals(Status.NEW)) {
                countNewSubTasks++;
            }
        }
        if (countDoneSubTasks == epic.getIdSubTasks().size()) {
            epic.setStatus(Status.DONE);
        } else if (countNewSubTasks == epic.getIdSubTasks().size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
