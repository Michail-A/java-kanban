package taskModel;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String title, String description) {
        super(title, description);
        subTasks = new ArrayList<>();
    }

    public Epic(int id, String title, String description, ArrayList<SubTask> subTasks) {
        super(id, title, description);
        this.subTasks = subTasks;
        updateStatus();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void updateStatus() {
        if (subTasks.isEmpty()) {
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
        if (countDoneSubTasks == subTasks.size()) {
            setStatus(Status.DONE);
        } else if (countNewSubTasks == subTasks.size()) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    public void updateSubTask(SubTask newSubTask, SubTask oldSubTask) {
        subTasks.remove(oldSubTask);
        subTasks.add(newSubTask);
        updateStatus();
    }

    public void deleteSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        updateStatus();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                ", subTasks=" + subTasks +
                '}';
    }
}
