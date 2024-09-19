package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> idSubTasks;

    public Epic(String title, String description) {
        super(title, description);
        idSubTasks = new ArrayList<>();
    }

    public Epic(int id, String title, String description, List<Integer> idSubTasks) {
        super(id, title, description);
        this.idSubTasks = idSubTasks;

    }

    public void addSubTask(SubTask subTask) {
        idSubTasks.add(subTask.getId());
    }

    public void updateStatus(List<SubTask> subTasks) {
        if (idSubTasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }
        int countDoneSubTasks = 0;
        int countNewSubTasks = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.DONE)) {
                countDoneSubTasks++;
            }
            if (subTask.getStatus().equals(Status.NEW)) {
                countNewSubTasks++;
            }
        }
        if (countDoneSubTasks == idSubTasks.size()) {
            setStatus(Status.DONE);
        } else if (countNewSubTasks == idSubTasks.size()) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    public void updateSubTask(SubTask newSubTask, SubTask oldSubTask) {
        idSubTasks.remove((Integer) oldSubTask.getId());
        idSubTasks.add((Integer) newSubTask.getId());
    }

    public void deleteSubTask(SubTask subTask) {
        idSubTasks.remove(subTask.getId());
    }

    public List<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(List<Integer> idSubTasks) {
        this.idSubTasks = idSubTasks;
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
