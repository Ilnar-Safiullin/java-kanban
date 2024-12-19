import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task = new Task("Сходить в магазин", Status.TO_DO);
        manager.addInMapTask(task.getId(), task); // добавляем задачу в Мапу
        Task task1 = new Task("Покушать лапши", Status.TO_DO);
        manager.addInMapTask(task1.getId(), task1);

        Epic epic = new Epic("Помыть машину", Status.TO_DO);
        manager.addInMapEpic(epic.getId(), epic);

        Subtask subtask = new Subtask("Доехать до автомойки", Status.TO_DO);
        manager.addSubtask(epic, subtask); //присваиваем субтаску ЕпикАйди Епика. Но по сути проще просто писать subtask.setEpicId(epic)
        manager.addInMapSubtask(subtask.getId(), subtask);
        Subtask subtasks = new Subtask("Оплатить комплекс", Status.IN_PROGRESS);
        manager.addSubtask(epic, subtasks);
        manager.addInMapSubtask(subtasks.getId(), subtasks);

        Epic epic2 = new Epic("Переезд", Status.TO_DO);
        manager.addInMapEpic(epic2.getId(), epic2);

        Subtask subtask1 = new Subtask("Собрать вещи", Status.IN_PROGRESS);
        manager.addSubtask(epic2, subtask1); //присваиваем субтаску ЕпикАйди Епика
        manager.addInMapSubtask(subtask1.getId(), subtask1);
        Subtask subtask2 = new Subtask("Заказать грузчиков", Status.IN_PROGRESS);
        manager.addSubtask(epic2, subtask2);
        manager.addInMapSubtask(subtask2.getId(), subtask2);

        manager.printAllTasks(); //Выводим все задачи

        int id = 3; //для теста, потом можно сделать с консоли
        Status status = Status.IN_PROGRESS; //для теста, потом можно сделать с консоли

        manager.changeStatusForId(id, status); //меняем статус задачи по id

        manager.searchForId(id); // ищем задачу по id

        int epicId = 0; // вводим epicId по которому хотим чекнуть подзадачи на статус

        manager.chekSubStatusAndChangeEpicStatus(epicId); // меняем статус Епика если все подзадачи выполнены

        manager.searchForSubtaskForEpicId(epicId);

        manager.removeTaskForId(id);

        manager.removeAllTask();
    }
}

/* Спасибо за прошлую обратную связь. В итоге сидя днями и ночами, исчерпывая кол-во бесплатных вопросов к нейросети
Яндекса. Все таки получилось собрать что-то похожее на решение. Мейн весь для теста как и писал ты делал, консоли не крутил
написано вроде как что ТаскМенеджер должен уметь добавлять задачи, незнаю толи это что нужно. Буду ждать ОС и что нужно
докрутить. Спасибо и Хорошего тебе Дня!
p.s. Тебе удобнее если я на гит пушу? или лучше по старинке папку просто кидать?
 */