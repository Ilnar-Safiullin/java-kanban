import TaskManager.TaskManager;
import Task.Epic;
import Task.Task;
import Task.Subtask;
import Task.Status;


public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task = new Task("Пополнить холодильник","Сходить в магазин");
        manager.addInMapTask(task);

        Epic epic = new Epic("Чистка машины","Помыть машину");
        manager.addInMapEpic(epic);
        Subtask subtask = new Subtask("Автомойка","Доехать до автомойки", epic.getId());
        manager.addInMapSubtask(subtask);

        subtask.setStatus(Status.IN_PROGRESS);

        Subtask subtask2 = new Subtask("Оплата","Оплатить комплекс", epic.getId());
        manager.addInMapSubtask(subtask2);

        subtask2.setStatus(Status.IN_PROGRESS);

        manager.getAllSubtaskForEpicId(epic.getId());
        manager.searchForIdTask(task.getId());
        manager.searchForIdEpic(32);

        manager.removeSubtaskForId(subtask.getId());
        manager.removeSubtaskForId(subtask2.getId());

        manager.removeEpicForId(epic.getId());

        manager.removeEpicForId(epic.getId());
        manager.removeSubtaskForId(subtask.getId());

        manager.removeTaskForId(534);

        manager.removeAllTask();
        manager.removeAllEpics();
        manager.removeAllSubtasks();
    }
}

/* Привет. Вроде все сделал. Спасибо)

 */