package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUP() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task = new Task("Test", "Test Task", Duration.ofMinutes(10), LocalDateTime.now());
        inMemoryTaskManager.addInMapTask(task);
        epic = new Epic("Test", "Test Epic");
        inMemoryTaskManager.addInMapEpic(epic);
        subtask = new Subtask("Test", "Test Subtask", epic.getId(), Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(15L));
        inMemoryTaskManager.addInMapSubtask(subtask);
    }

    @Test
    public void updateEpicStatusTest() {
        subtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "updateStatusForEpic работает не корректно");
        assertEquals(subtask.getStatus(), Status.IN_PROGRESS, "updateSubtask работает не корректно");
        subtask.setStatus(Status.DONE);
        inMemoryTaskManager.updateEpic(epic);
        assertEquals(epic.getStatus(), Status.DONE, "updateStatusForEpic работает не корректно");
        assertEquals(subtask.getStatus(), Status.DONE, "updateEpic работает не корректно");
        Subtask subtask2 = new Subtask("Тест 2", "Тест 2", epic.getId(), Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(40L));
        inMemoryTaskManager.addInMapSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS,
                "updateStatusForEpic при добавление новой Субатаски работает не корректно");
    }

    @Test
    public void updateEpicTimeTest() {
        assertEquals(epic.getStartTime(), subtask.getStartTime(), "Эпик не обновил startTime после добавления Субтаски");
        assertEquals(epic.getDuration(), subtask.getDuration(), "Эпик не обновил duration после добавления Субтаски");
        assertEquals(epic.getEndTime(), subtask.getEndTime(), "Эпик не обновил EndTime после добавления Субтаски");
    }

    @Test
    public void isTaskIntersectionTest() {
        Task task2 = new Task("Test", "Test Task", Duration.ofMinutes(10), LocalDateTime.now());
        inMemoryTaskManager.addInMapTask(task2);
        assertEquals(inMemoryTaskManager.getTasks().size(), 1, "Добавляет задачи с пересечением по времени");
    }
}
