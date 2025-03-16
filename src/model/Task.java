package model;

import manager.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Task(String title, String description, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
        endTime = calculateEndTime();
    }

    public Task(int id, String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        endTime = calculateEndTime();
    }

    public Task(int id, String title, String description, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        endTime = calculateEndTime();
        this.status = Status.NEW;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeTask getTypeTask() {
        return TypeTask.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime calculateEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public String toStringForSave() {
        return id + "," + getTypeTask() + "," + title + "," + status
                + "," + description + "," + duration + "," + startTime + "," + endTime;
    }

}
