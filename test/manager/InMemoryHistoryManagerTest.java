package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    public void saveHistory() {
        Task task = new Task("Тест", "Тест");
        inMemoryHistoryManager.add(task);
        assertEquals(1, inMemoryHistoryManager.getHistory().size(),
                " InMemoryHistoryManager Не сохраняет историю просмотров");
    }

    @Test
    public void removeEvery11History() {
        for (int i = 1; i <= 11; i++) {
            Task task = new Task("Тест", "Тест");
            inMemoryHistoryManager.add(task);
        }
        assertEquals(10, inMemoryHistoryManager.getHistory().size(),
                "В Листе хранится больше 10 записей");
    }

    @Test
    public void save10History() {
        for (int i = 1; i <= 10; i++) {
            Task task = new Task("Тест", "Тест");
            inMemoryHistoryManager.add(task);
        }
        assertEquals(10, inMemoryHistoryManager.getHistory().size(),
                "Лист не может хранить 10записей ");
    }

}
