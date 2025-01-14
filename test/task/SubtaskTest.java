package task;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {
    private TaskManager taskManager;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
        epic = new Epic("Чистка машины", "Помыть машину");
        taskManager.addInMapEpic(epic);
        subtask = new Subtask("Доехать до автомойки", "Оплатить комплекс", epic.getId());
        taskManager.addInMapSubtask(subtask);
    }

    @Test
    public void addSubtaskInMap() {
        assertEquals(1, taskManager.getSubtasks().size(), "Субтаска не была добавлена в Мапу");
    }

    @Test
    public void equalSubtaskToEachOtherForIdAndDifferentDescriptions() {
        Subtask subtask2 = new Subtask("Другое описание но Айди одинаковый", "И здесь описание другое",
                epic.getId());
        subtask2.setId(subtask.getId()); // Назначали subtask2 АЙди от subtask
        assertEquals(subtask2, subtask, "Субтаски с одинаковым Айди стали разными");
    }

    @Test
    public void notEqualSubtaskToDifferentIpAndEqualDescriptions() {
        Subtask subtask2 = new Subtask("Чистка машины", "Помыть машину", epic.getId());
        taskManager.addInMapSubtask(subtask2);
        assertNotEquals(subtask, subtask2, "Субтаски с разными АЙди равны");
    }

    @Test
    public void testAddSubtaskInEpic() {
        assertEquals(1, epic.getSubTaskIdList().size(), "Субтаск не добавился в лист Эпика");
    }

    @Test
    public void haveSubtaskEpicidForEpic() {
        assertEquals(epic.getId(), subtask.getEpicId(), "Субтаск не получил значение ЕпикАйди от Айди Епика");
    }

    @Test
    public void notSubtaskMakeYourEpic() {
        Subtask subtask2 = new Subtask("Тест", "Тест", subtask.getId());
        taskManager.addInMapSubtask(subtask2);
        assertNull(taskManager.getSubtasksForId(subtask2.getId()), "Субтаск стал для Субтаска Эпиком");
    }

}
