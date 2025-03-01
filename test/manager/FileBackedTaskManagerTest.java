package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = new File("./resources/task.csv");
        taskManager = new FileBackedTaskManager(file);
        task = new Task("Test", "Test Task", Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.addInMapTask(task);
        epic = new Epic("Test", "Test Epic");
        taskManager.addInMapEpic(epic);
        subtask = new Subtask("Test", "Test Subtask", epic.getId(), Duration.ofMinutes(10),
                LocalDateTime.now().minusMinutes(15L));
        taskManager.addInMapSubtask(subtask);
    }

    @Test
    public void loadFromFileAndSaveTest() {
        FileBackedTaskManager backedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(taskManager.getTasks(), backedTaskManager.getTasks(),
                "Запись/Чтение файла не корректны с Тасками");
        assertEquals(taskManager.getEpics(), backedTaskManager.getEpics(),
                "Запись/Чтение файла не корректны c Эпиками");
        assertEquals(taskManager.getSubtasks(), backedTaskManager.getSubtasks(),
                "Запись/Чтение файла не корректны с Субтасками");
        assertEquals(2, backedTaskManager.getPrioritizedTasks().size(),
                "Запись/Восстановление списка приоритетов не корректна");
    }
}
