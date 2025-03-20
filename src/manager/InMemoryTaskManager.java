package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private int id = 0;
    protected final HistoryManager historyManager;
    protected final Set<Task> prioritizedTasks = new TreeSet<>((Task t1, Task t2) -> {
        if (t1.getStartTime().isBefore(t2.getStartTime())) {
            return -1;
        } else if (t1.getStartTime().isAfter(t2.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
    });

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
        deleteFromHistory(List.copyOf(tasks.keySet()));
        deleteFormPrioritizedTasks(List.copyOf(tasks.values()));
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        deleteFromHistory(List.copyOf(epics.keySet()));
        deleteFromHistory(List.copyOf(subTasks.keySet()));
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        deleteFromHistory(List.copyOf(subTasks.keySet()));
        deleteFormPrioritizedTasks(List.copyOf(subTasks.values()));
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasks();
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public void addTask(Task task) {
        if (task.getStartTime() != null) {
            checkIntersection(task);
        }
        task.setId(id);
        tasks.put(id, task);
        id++;
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
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
        if (subTask.getStartTime() != null) {
            checkIntersection(subTask);
        }
        subTasks.put(id, subTask);
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
        id++;
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        updateEpicStatus(epic);
    }

    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(tasks.get(task.getId()));
        if (task.getStartTime() != null) {
            checkIntersection(task);
        }
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
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
        prioritizedTasks.remove(oldSubTask);
        if (subTask.getStartTime() != null) {
            checkIntersection(subTask);
        }
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.updateSubTask(subTask, oldSubTask);
        updateEpicStatus(epic);
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        List<SubTask> subTasksByEpic = getSubTasksByEpicId(id);
        historyManager.remove(id);
        epics.remove(id);
        for (SubTask subTask : subTasksByEpic) {
            prioritizedTasks.remove(subTask);
            historyManager.remove(subTask.getId());
            subTasks.remove(subTask.getId());
        }
    }

    @Override
    public void deleteSubTasksById(int id) {
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.deleteSubTask(subTasks.get(id));
        prioritizedTasks.remove(subTasks.get(id));
        subTasks.remove(id);
        historyManager.remove(id);
        updateEpicStatus(epic);
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int id) {
        List<Integer> subTasksIdByEpic = epics.get(id).getIdSubTasks();
        List<SubTask> subTasksByEpic = new ArrayList<>();
        for (Integer idSubTask : subTasksIdByEpic) {
            subTasksByEpic.add(subTasks.get(idSubTask));
        }
        return subTasksByEpic;
    }


    protected void updateEpicStatus(Epic epic) {
        if (epic.getIdSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
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
        calculateEpicTime(epic);
    }

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    protected void calculateEpicTime(Epic epic) {
        List<SubTask> subTasksByEpicSorted = epic.getIdSubTasks().stream()
                .map(subTasks::get)
                .filter(s -> s.getStartTime() != null)
                .filter(s -> s.getDuration() != null)
                .sorted(Comparator.comparing(Task::getStartTime))
                .toList();
        if (!subTasksByEpicSorted.isEmpty()) {
            LocalDateTime startTime = subTasksByEpicSorted.getFirst().getStartTime();
            LocalDateTime endTime = subTasksByEpicSorted.getLast().getEndTime();

            Duration sumDuration = subTasksByEpicSorted.stream()
                    .map(Task::getDuration)
                    .reduce(Duration.ZERO, Duration::plus);

            epic.setStartTime(startTime);
            epic.setDuration(sumDuration);
            epic.setEndTime(endTime);
        }
    }

    private void deleteFromHistory(List<Integer> ids) {
        for (Integer id : ids) {
            historyManager.remove(id);
        }
    }

    private void deleteFormPrioritizedTasks(List<Task> tasks) {
        tasks.forEach(prioritizedTasks::remove);
    }

    protected void putInMaps(Task task) {
        switch (task.getTypeTask()) {
            case TASK:
                tasks.put(task.getId(), task);
                break;
            case EPIC:
                epics.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                subTasks.put(task.getId(), (SubTask) task);
                break;
        }
    }

    protected void setId(int id) {
        this.id = id;
    }

    private int getId() {
        return id;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void checkIntersection(Task task) {

        for (Task task1 : prioritizedTasks) {
            boolean noIntersection = !task.getStartTime().isBefore(task1.getEndTime()) || !task.getEndTime().isAfter(task1.getStartTime());
            if (!noIntersection) {
                throw new IllegalArgumentException("Время  задачи уже занято");
            }
        }
    }
}
