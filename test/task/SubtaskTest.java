package task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {
    private Subtask subtask;

    @BeforeEach
    public void setUp() {
        subtask = new Subtask("Доехать до автомойки", "Оплатить комплекс", 1, Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(15L));
        subtask.setId(2);
    }

    @Test
    public void equalSubtaskToEachOtherForIdAndDifferentDescriptions() {
        Subtask subtask2 = new Subtask("Другое описание но Айди одинаковый", "тест", subtask.getId(), Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(15L));
        subtask2.setId(subtask.getId()); // Назначали subtask2 АЙди от subtask
        assertEquals(subtask2, subtask, "Субтаски с одинаковым Айди стали разными");
    }

    @Test
    public void notEqualSubtaskToDifferentIpAndEqualDescriptions() {
        Subtask subtask2 = new Subtask("Чистка машины", "Помыть машину", 1, Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(15L));
        subtask2.setId(3);
        assertNotEquals(subtask, subtask2, "Субтаски с разными АЙди равны");
    }

    @Test
    public void testAddSubtaskInEpic() {
        Epic epic = new Epic("Чистка машины", "Помыть машину");
        epic.getSubTaskIdList().add(subtask.getId());
        assertEquals(1, epic.getSubTaskIdList().size(), "Субтаск невозможно добвить в лист Эпика");
    }

    @Test
    public void haveSubtaskEpicidForEpic() {
        Epic epic = new Epic("Чистка машины", "Помыть машину");
        epic.setId(1);
        assertEquals(epic.getId(), subtask.getEpicId(), "Субтаск не получил значение ЕпикАйди от Айди Епика");
    }

    // Тест что Субтаск не может сам себе эпиком стать перенес в InMemoryTaskManagerTest, так как субтаск крепится в к Эпику
    // только в момент добавления в мапу.

}
