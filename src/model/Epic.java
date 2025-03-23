package model;

import manager.TypeTask;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> idSubTasks;

    public Epic(String title, String description) {
        super(title, description, Duration.ofMinutes(0), null);
        idSubTasks = new ArrayList<>();
    }

    public Epic(int id, String title, String description, List<Integer> idSubTasks) {
        super(id, title, description, null, null);
        this.idSubTasks = idSubTasks;

    }

    public Epic(int id, String title, String description) {
        super(id, title, description, null, null);
        idSubTasks = new ArrayList<>();

    }

    public void addSubTask(SubTask subTask) {
        idSubTasks.add(subTask.getId());
    }

    public void updateSubTask(SubTask newSubTask, SubTask oldSubTask) {
        idSubTasks.remove((Integer) oldSubTask.getId());
        idSubTasks.add((Integer) newSubTask.getId());
    }

    public void deleteSubTask(SubTask subTask) {
        idSubTasks.remove((Integer) subTask.getId());
    }

    public List<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void removeAllSubTasks() {
        idSubTasks = new ArrayList<>();
        setStatus(Status.NEW);
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                ", subTasks=" + idSubTasks +
                '}';
    }
}
