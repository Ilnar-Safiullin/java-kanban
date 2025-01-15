package task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;

    @BeforeEach
    public void setUp() {
        epic = new Epic("Чистка машины", "Помыть машину");
        epic.setId(1);
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
        task.setId(2);
        epic.setId(task.getId()); // Назначали epic2 номер АЙди от task
        assertNotEquals(epic, task, "Эпик и Таск с одинаковым АЙди стали равны");
    }

    @Test
    public void notEqualEpicToDifferentIpAndEqualDescriptions() {
        Epic epic3 = new Epic("Чистка машины", "Помыть машину");
        epic3.setId(2);
        assertNotEquals(epic, epic3, "Эпики с одинаковым описанием но разным Айди стали Равны");
    }

    @Test
    public void testAddSubtaskInEpic() {
        Subtask subtask = new Subtask("Выехать", "Доехать до автомойки", epic.getId());
        epic.getSubTaskIdList().add(subtask.getId());
        assertEquals(1, epic.getSubTaskIdList().size(), "Айди Сабтаски не добавить в Эпик");
    }

}