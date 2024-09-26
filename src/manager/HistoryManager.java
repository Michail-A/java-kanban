package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {

    void writeHistory(Task task);

    List<Task> getHistory();
}
