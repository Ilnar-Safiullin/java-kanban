import manager.*;
import task.Epic;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager maneger = Managers.getDefault();
        Task task1 = new Task("Тест 1", "Test 1");
        maneger.addInMapTask(task1);
        Task task2 = new Task("Test 2", "Test2");
        maneger.addInMapTask(task2);
        Task task3 = new Task("Test 3", "Test 3");
        maneger.addInMapTask(task3);
        Epic epic1 = new Epic("Test", "Test");
        maneger.addInMapEpic(epic1);
        Subtask subtask1 = new Subtask("Test", "Test", epic1.getId());
        maneger.addInMapSubtask(subtask1);
        maneger.getTaskForId(task1.getId());
        maneger.getTaskForId(task1.getId());
        maneger.getEpicForId(epic1.getId());
        maneger.getSubtasksForId(subtask1.getId());
        maneger.getHistory();
    }
}
/* Еще раз привет)) Большой спасибо за советы и обратную связь! Вроде все поправил. Методы getFirst() и getLast() решил оставить
    как раз ты сказал что в методе remove() нужно чтобы при удалении единственной задачи last и first нужно также скинуть
    вот для этой цели написал тест и смотрел как работает. Хорошего дня!)
 */


