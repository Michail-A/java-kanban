package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();

    @Override
    public void writeHistory(Task task) {
        if (history.size() > 10) {
            history.removeFirst();
        }

        if (task != null) {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
