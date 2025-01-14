package task;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private TaskManager taskManager;
    private Epic epic;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
        epic = new Epic("Чистка машины", "Помыть машину");
        taskManager.addInMapEpic(epic);
    }

    @Test
    public void addEpicInMap() {
        assertEquals(1, taskManager.getEpics().size());
    }

    @Test
    public void equalEpicToEachOtherForIdAndDifferentDescriptions() {
        Epic epic2 = new Epic("Другое описание но Айди одинаковый", "И здесь описание другое");
        epic2.setId(epic.getId()); // Назначали epic2 номер АЙди от epic
        assertEquals(epic2, epic, "Епики с одинаковым АЙди не равны");
    }

    @Test
    public void notEqualEpicAndTaskForEqualId() {
        Task task = new Task("Чистка машины", "Помыть машину");
        taskManager.addInMapTask(task);
        epic.setId(task.getId()); // Назначали epic2 номер АЙди от task
        assertNotEquals(epic, task, "Эпик и Таск с одинаковым АЙди стали равны");
    }

    @Test
    public void notEqualEpicToDifferentIpAndEqualDescriptions() {
        Epic epic3 = new Epic("Чистка машины", "Помыть машину");
        taskManager.addInMapEpic(epic3);
        assertNotEquals(epic, epic3, "Эпики с одинаковым описанием но разным Айди стали Равны");
    }

    @Test
    public void testAddSubtaskInEpic() {
        Subtask subtask = new Subtask("Выехать", "Доехать до автомойки", epic.getId());
        taskManager.addInMapSubtask(subtask);
        assertEquals(1, epic.getSubTaskIdList().size(), "Сабтаски не добавляются в Эпик");
    }

    // Тест что из ТЗ что Эпик нельзя добавить внутрь себя как подзадачу, куратор сказал не нужно делать, это опечатка
    // в задании, т.к. не имеет смысла, потомучто у нас метод добавления Субтаски отличается от Эпика. Даже конструктора
    // у Эпика нет такого как у Субтаски, чтобы попробывать Эпик в Эпик впихнуть
}