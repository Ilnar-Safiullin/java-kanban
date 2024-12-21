public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        manager.newTask("Пополнить холодильник","Сходить в магазин");

        Epic epic = new Epic("Чистка машины","Помыть машину");
        manager.addInMapEpic(epic);

        Subtask subtask = new Subtask("Автомойка","Доехать до автомойки");
        manager.addInMapSubtask(subtask);
        manager.addSubtaskIdInEpicId(subtask, epic.getId());
        manager.changeStatusForIdSubtask(subtask.getId(), Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask("Оплата","Оплатить комплекс");
        manager.addInMapSubtask(subtask2);
        manager.addSubtaskIdInEpic(subtask2, epic);
        manager.changeStatusForIdSubtask(subtask2.getId(), Status.IN_PROGRESS);

        manager.searchAllSubtaskInEpicId(epic.getId());
        manager.searchForIdTask(1);
        manager.removeSubtasksForId(3);
        manager.removeEpicsForId(2);
        manager.searchForIdEpic(32);
    }
}

/* Привет, Спасибо тебе большое за обратную связь и комментарии подробные с ответами на вопросы. Очень сильно помогает!.
 Вроде все работает как надо. Хотел создавать Епики и Субтаски в ТаскМенеджере по примеру Тасков. Но не придумал как прикрутить
 сразу при создании Субтасков епикаАйди Епиков, и добавить еще потом Айди Субтаска Внутрь Епика в лист. Поэтому создаю все кроме
 Тасков по старому.

Ты писал что модификаторы в Таске можно протектед оставить как я понял. Поэтому большинство методов без гетеров и сетеров
Или лучше всетаки сразу привыкать все эти поля только через геттеры и сеттеры делать, даже если протектед?

Мейн у меня для тестов. Или перед отправкой удалять, он тебе совсем не нужен и только раздражает?)

Хороших тебе Новогодних выходных !

 */