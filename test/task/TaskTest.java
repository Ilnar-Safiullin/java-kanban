package task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private Task task;

    @BeforeEach
    public void setUp() {
        task = new Task("Закрыть долги", "Оплатить все долги");
        task.setId(1);
    }

    @Test
    public void equalTaskToEachOtherForIdAndDifferentDescriptions() {
        Task task2 = new Task("Другое описание но Айди одинаковый", "И здесь описание другое");
        task2.setId(task.getId()); // Назначали task2 номер АЙди от task
        assertEquals(task2, task, "Таски с разным Описанием и одинаковым Айди не равны");
    }

    @Test
    public void notEqualTaskToDifferentIpAndEqualDescriptions() {
        Task task3 = new Task("Чистка машины", "Помыть машину");
        task3.setId(2);
        assertNotEquals(task3, task, "Таски с одинаковым Описанием но разным Айди равны");
    }

}
