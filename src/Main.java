import manager.FileBackedTaskManager;
import manager.Managers;
import model.Task;
import util.Constants;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistory());
        Task task = new Task("1", "2");
        manager.addTask(task);
        manager.getTaskById(task.getId());

    }
}
