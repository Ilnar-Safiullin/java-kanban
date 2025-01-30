package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
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
        task.setId(0);
        inMemoryHistoryManager.add(task);
        Epic epic1 = new Epic("Test", "Test");
        epic1.setId(1);
        inMemoryHistoryManager.add(epic1);
        Subtask subtask1 = new Subtask("Test", "Test", epic1.getId());
        subtask1.setId(2);
        inMemoryHistoryManager.add(subtask1);
        assertEquals(3, inMemoryHistoryManager.getHistory().size(),
                " InMemoryHistoryManager Не сохраняет историю просмотров");
    }

    @Test
    public void saveUniqueHistory() {
        Task task = new Task("Тест", "Тест");
        task.setId(1);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task);
        assertEquals(1, inMemoryHistoryManager.getHistory().size(),
                " InMemoryHistoryManager дублирует айди в истории");
    }

    @Test
    public void removeAndGetHistoryTest() {
        Task task = new Task("Тест", "Тест");
        task.setId(0);
        Task task2 = new Task("Тест", "Тест");
        task2.setId(1);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task2);
        assertEquals(2, inMemoryHistoryManager.getHistory().size(),
                " GetHistory работает не корректно");
        inMemoryHistoryManager.remove(task.getId());
        assertEquals(1, inMemoryHistoryManager.getHistory().size(),
                "InMemoryHistoryManager не удаляет задачи из истории");
    }

    @Test
    public void linkLastTest() {
        Task task = new Task("Тест", "Тест");
        task.setId(0);
        inMemoryHistoryManager.add(task);
        assertNotNull(inMemoryHistoryManager.getFirst(), "linkLast не присвоил первой Node <first>");
        assertNotNull(inMemoryHistoryManager.getLast(), "linkLast не присвоил первой Node <last>");
    }
}
// Хотел добавить тест на node.prev и node.next, что он адекватно присваивает эти значения но не смог сделать это.
// Не могу создать ноду Node node = inMemoryHistoryManager.getlast(); Решил что в гет хистори впринципе это преврка и так проходит