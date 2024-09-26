package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private int id = 0;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasks();
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.writeHistory(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.writeHistory(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.writeHistory(subTask);
        return subTask;
    }

    @Override
    public void addTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        id++;
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        id++;
    }

    @Override
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

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic updatedEpic = epics.get(epic.getId());
        updatedEpic.setTitle(epic.getTitle());
        updatedEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        SubTask oldSubTask = subTasks.get(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.updateSubTask(subTask, oldSubTask);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        List<SubTask> subTasksByEpic = getSubTasksByEpicId(id);
        epics.remove(id);
        for (SubTask subTask : subTasksByEpic) {
            subTasks.remove(subTask.getId());
        }
    }

    @Override
    public void deleteSubTasksById(int id) {
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.deleteSubTask(subTasks.get(id));
        subTasks.remove(id);
        updateEpicStatus(epic);
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int id) {
        List<SubTask> subTasksByEpic = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == id) {
                subTasksByEpic.add(subTask);
            }
        }
        return subTasksByEpic;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
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
