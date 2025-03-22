package model;

import manager.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(int id, String title, String description, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(id, title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }

    @Override
    public String toStringForSave() {
        return super.toStringForSave() + "," + epicId;
    }
}
