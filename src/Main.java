import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        manager.addTask(task1);
        manager.addTask(task2);
        System.out.println("Получение всех задач:");
        System.out.println(manager.getTasks());
        System.out.println();

        System.out.println("Получение задачи по ID:");
        System.out.println(manager.getTaskById(task1.getId()));
        System.out.println();

        System.out.println("Удаление всех задач:");
        manager.deleteTasks();
        System.out.println(manager.getTasks());
        System.out.println();

        System.out.println("Обновление задачи:");
        manager.addTask(task1);
        Task updateTask = new Task("update 1", "update", task1.getId(), Status.IN_PROGRESS);
        manager.updateTask(updateTask);
        System.out.println(manager.getTasks());
        System.out.println();

        System.out.println("Удаление задачи по ID");
        manager.deleteTaskById(task1.getId());
        System.out.println(manager.getTasks());
        System.out.println();


        System.out.println("Получение всех эпиков");
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", epic1.getId());
        manager.addEpic(epic1);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        System.out.println(manager.getEpics());
        System.out.println();

        System.out.println("Получение эпика по id");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

        System.out.println("Удаление всех эпиков");
        manager.deleteEpics();
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());
        System.out.println();

        System.out.println("Обновление эпика по id");
        manager.addEpic(epic1);
        Epic updateEpic = new Epic(epic1.getId(), "Обновленный эпик", "Обновленный эпик", epic1.getIdSubTasks());
        manager.updateEpic(updateEpic);
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

        System.out.println("Обновление подзадачи");
        subTask1 = new SubTask("Подзадача 1", "Описание", epic1.getId());
        subTask2 = new SubTask("Подзадача 2", "Описание", epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        SubTask updateSubTask = new SubTask("Обновленная подзадача", "Описание",
                subTask1.getId(), Status.IN_PROGRESS, subTask1.getEpicId());
        manager.updateSubtask(updateSubTask);
        System.out.println(manager.getSubTaskById(subTask1.getId()));
        System.out.println();

        System.out.println("Проверка статуса эпика IN PROGRSS");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

        System.out.println("Удаление всех сабтасков");
        manager.deleteSubTasks();
        System.out.println(manager.getSubTasks());

        System.out.println("Проверка статуса эпика DONE");
        subTask1 = new SubTask("Подзадача 1", "Описание", epic1.getId());
        manager.addSubTask(subTask1);
        updateSubTask = new SubTask("Обновленная подзадача", "Описание",
                subTask1.getId(), Status.DONE, subTask1.getEpicId());
        manager.updateSubtask(updateSubTask);
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

    }
}
