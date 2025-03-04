package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Тест", "Тест");
        epic = new Epic("Тест", "Тест");
        subtask = new Subtask("Тест", "Тест", 5, Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(15L));
        task.setId(0);
        epic.setId(1);
        subtask.setId(2);
    }

    @Test
    public void saveHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        assertEquals(3, historyManager.getHistory().size(),
                " historyManager Не сохраняет историю просмотров");
    }

    @Test
    public void saveUniqueHistory() {
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(),
                " historyManager дублирует айди в истории");
    }

    @Test
    public void removeAndGetHistoryTest() {
        historyManager.add(task);
        historyManager.add(epic);
        assertEquals(2, historyManager.getHistory().size(),
                " getHistory работает не корректно");
        historyManager.remove(task.getId());
        assertEquals(1, historyManager.getHistory().size(),
                "historyManager не удаляет задачи из истории");
    }

    @Test
    public void removeFirstTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(task.getId());
        assertEquals(List.of(epic, subtask), historyManager.getHistory(),
                "после удаления из начала истории, изменился порядок истории просмотров задач");
    }

    @Test
    public void removeMiddleTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(epic.getId());
        assertEquals(List.of(task, subtask), historyManager.getHistory(),
                "после удаления из середины истории, изменился порядок истории просмотров задач");
    }

    @Test
    public void removeLastTest() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(subtask.getId());
        assertEquals(List.of(task, epic), historyManager.getHistory(),
                "после удаления из конца истории, изменился порядок истории просмотров задач");
    }

    @Test
    public void removalOfTheOnlyOneNodeTest() {
        Task task = new Task("Тест", "Тест");
        task.setId(0);
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertEquals(0, historyManager.getHistory().size(),
                "при удалении единственной ноды, не сделали first = null");
    }
}
