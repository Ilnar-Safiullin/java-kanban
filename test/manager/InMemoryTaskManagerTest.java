package manager;

import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Test", "Test Task", Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.addInMapTask(task);
        epic = new Epic("Test", "Test Epic");
        taskManager.addInMapEpic(epic);
        subtask = new Subtask("Test", "Test Subtask", epic.getId(), Duration.ofMinutes(10),
                LocalDateTime.now().minusMinutes(15L));
        taskManager.addInMapSubtask(subtask);
    }
}
