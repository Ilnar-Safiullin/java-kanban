package manager;

import org.junit.jupiter.api.Test;
import task.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    File file;
    FileBackedTaskManager managerRestored;

    @Test
    public void loadFromFileAndSaveTest() {
        file = new File("./resources/task.csv");
        managerRestored = new FileBackedTaskManager(file);
        Task task = new Task("Test", "Test Task", Duration.ofMinutes(10), LocalDateTime.now());
        Epic epic = new Epic(2, "Epic", "Test");
        Subtask subtask = new Subtask(3, "Subtask", "Test", 2, Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(15L));
        managerRestored.addInMapTask(task);
        managerRestored.addInMapEpic(epic);
        managerRestored.addInMapSubtask(subtask);
        FileBackedTaskManager backedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(managerRestored.getTasks(), backedTaskManager.getTasks(),
                "Запись/Чтение файла не корректны с Тасками");
        assertEquals(managerRestored.getEpics(), backedTaskManager.getEpics(),
                "Запись/Чтение файла не корректны c Эпиками");
        assertEquals(managerRestored.getSubtasks(), backedTaskManager.getSubtasks(),
                "Запись/Чтение файла не корректны с Субтасками");
    }
}
