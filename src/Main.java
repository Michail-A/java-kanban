import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(Paths.get("src/data.csv"),
                Paths.get("src/data_history.csv"));
        /*Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        manager.addTask(task1);
        manager.addTask(task2);
*/
        Task task3 = new Task("Задача 2", "Описание 2");
        manager.addTask(task3);
    System.out.println(manager.getTasks());
    }
}
