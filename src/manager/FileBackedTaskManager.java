package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final Path DATA = Paths.get("src/data.csv");
    private static final Path DATA_HISTORY = Paths.get("src/data_history.csv");

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    private String taskToString(Task task) {
        String line = task.getId() + "," + task.getTypeTask() + "," + task.getTitle() + "," + task.getStatus()
                + "," + task.getDescription();
        if (task.getTypeTask() == TypeTask.SUBTASK) {
            SubTask subTask = (SubTask) task;
            line = line + "," + subTask.getEpicId();
        }
        return line;
    }

    private static Task taskFromString(String line) {
        String[] split = line.split(",");
        int id = Integer.parseInt(split[0]);
        TypeTask type = TypeTask.valueOf(split[1]);
        String title = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];
        Task task = null;
        switch (type) {
            case TASK:
                task = new Task(id, title, description, status);
                break;
            case EPIC:
                task = new Epic(id, title, description);
                break;
            case SUBTASK:
                task = new SubTask(id, title, description, status, Integer.parseInt(split[5]));
                break;
        }
        return task;
    }

    private static List<Integer> historyFromString(String line) {
        List<Integer> taskIdInHistory = new ArrayList<>();
        if (!line.isBlank() && !line.isEmpty()) {
            String[] arrayLine = line.split(" ");
            for (String s : arrayLine) {
                taskIdInHistory.add(Integer.parseInt(s));
            }
        }
        return taskIdInHistory;
    }

    private void recoverIdAndSubTaskInEpic(Map<Integer, Task> tasks) {
        int maxId = 0;
        for (Task task : tasks.values()) {

            if (task.getId() > maxId) {
                maxId = task.getId();
            }

            if (task.getTypeTask() == TypeTask.SUBTASK) {
                SubTask subTask = (SubTask) task;
                Epic epic = (Epic) tasks.get(subTask.getEpicId());
                epic.addSubTask(subTask);
                super.updateEpicStatus(epic);
            }
        }
        super.setId(maxId + 1);
    }

    private void recoverHistory(List<Integer> ids, Map<Integer, Task> tasks, HistoryManager historyManager) {
        for (Integer id : ids) {
            historyManager.add(tasks.get(id));
        }
    }

    public void save() {
        String tittle = "id,type,title,status,description,epicId";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA.toFile()));
             BufferedWriter bwHistory = new BufferedWriter(new FileWriter(DATA_HISTORY.toFile()))) {
            bw.write(tittle + "\n");
            for (Task task : super.getTasks()) {
                bw.write(taskToString(task) + "\n");
            }
            for (Epic epic : super.getEpics()) {
                bw.write(taskToString(epic) + "\n");
            }
            for (SubTask subTask : super.getSubTasks()) {
                bw.write(taskToString(subTask) + "\n");
            }
            StringBuilder builder = new StringBuilder();
            for (Integer id : super.getHistoryIds()) {
                builder.append(id).append(" ");
            }
            bwHistory.write(builder.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(Path data, Path dataHistory) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTaskManager manager = new FileBackedTaskManager(historyManager);
        Map<Integer, Task> tasks = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(data);
            for (int i = 1; i < lines.size(); i++) {
                Task task = taskFromString(lines.get(i));
                manager.putInMaps(task);
                tasks.put(task.getId(), task);
            }
            manager.recoverIdAndSubTaskInEpic(tasks);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + data.getFileName());
        }

        try {
            String stringHistory = Files.readString(dataHistory);
            manager.recoverHistory(historyFromString(stringHistory), tasks, historyManager);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + dataHistory.getFileName());
        }
        return manager;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTasksById(int id) {
        super.deleteSubTasksById(id);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }
}
