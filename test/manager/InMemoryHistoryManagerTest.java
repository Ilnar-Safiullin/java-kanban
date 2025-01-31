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
    public void removeFirstMiddleLastTest() {
        Task task = new Task("Тест", "Тест");
        Epic epic = new Epic("Тест", "Тест");
        Subtask subtask = new Subtask("Тест", "Тест", 5);
        Task task2 = new Task("Тест", "Тест");
        Task task3 = new Task("Тест", "Тест");
        task.setId(0);
        epic.setId(1);
        subtask.setId(2);
        task2.setId(3);
        task3.setId(4);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.remove(epic.getId());
        assertEquals(task, inMemoryHistoryManager.getHistory().getFirst(),
                "после удаления из середины истории, изменился порядок истории просмотров задач");
        assertEquals(task3, inMemoryHistoryManager.getHistory().getLast(),
                "после удаления из середины истории, изменился порядок истории просмотров задач");
        inMemoryHistoryManager.remove(task.getId());
        assertEquals(subtask, inMemoryHistoryManager.getHistory().getFirst(),
                "после удаления из начала истории, изменился порядок истории просмотров задач");
        assertEquals(task3, inMemoryHistoryManager.getHistory().getLast(),
                "после удаления из начала истории, изменился порядок истории просмотров задач");
        inMemoryHistoryManager.remove(task3.getId());
        assertEquals(subtask, inMemoryHistoryManager.getHistory().getFirst(),
                "после удаления из конца истории, изменился порядок истории просмотров задач");
        assertEquals(task2, inMemoryHistoryManager.getHistory().getLast(),
                "после удаления из конца истории, изменился порядок истории просмотров задач");
    }

    @Test
    public void removalOfTheOnlyOneNodeTest() {
        Task task = new Task("Тест", "Тест");
        task.setId(0);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.remove(task.getId());
        assertNull(inMemoryHistoryManager.getFirst(), "При удалении единственной ноды, не сделали first = null");
        assertNull(inMemoryHistoryManager.getLast(), "При удалении единственной ноды, не сделали last = null");
    }
}
