import manager.TaskManager;
import task.Epic;
import task.Task;
import task.Subtask;
import task.Status;


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

/* Привет. То что удалить все лишнее, я так понял это про гитахаб, в самой идеи у меня нет больше классов кроме текущих.
В гитхабе удалил лишнее. Так как это уже последняя пятая попытка, методы обновления я у курратора также попросил чекнуть перед отправкой
вроде сказал то. Вообще методы обновления я не понял зачем тут нужны. Мы же если надо можем также просто через setName, setStatus
любое поле обновить. Так же ждал все эти дни не отправлял у всех кто прошел спрашивал про обновление, сказали да такой вариант прошел.
хотя еще были варианты к примеру для task, просто в одну строку tasks.put(task.getId(), task);
у эпика:
(Epic epic)
Epic updEpic = epics.get(epic.getId());
updEpic.setName(epic.getName());
updEpic.setDescription(epic.getDescription());
но сказали тот вариант который я щас отправил верный.

Просто не совсем понял что делает это обновление, я думал оно параметром должно новый обьект класса Таск к примеру принимать и заменять текущий Таск, с тем же АЙди
но сказали сеттеры норм.

Спасибо и хороших тебе праздников !

 */