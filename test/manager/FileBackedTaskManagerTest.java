package manager;

import org.junit.jupiter.api.Test;
import task.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    File file;
    FileBackedTaskManager managerRestored;

    @Test
    public void loadFromFileTest() {
        file = new File(".\\resources\\task.csv");
        managerRestored = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, managerRestored.getTasks().size(), "loadFromFile() не восстановил Таски");
        assertEquals(1, managerRestored.getEpics().size(), "loadFromFile() не восстановил Епики");
        assertEquals(1, managerRestored.getSubtasks().size(), "loadFromFile() не восстановил Субтаски");
        assertEquals(1, managerRestored.getEpicForId(2).getSubTaskIdList().size(),
                "loadFromFile не связал Эпик с Субтаской");

    /*  Содержание файла "task.csv"
    id,type,name,status,description,epic
    1,TASK,Test 3,NEW,Test
    2,EPIC,Test 3,NEW,Test
    4,SUBTASK,Test,NEW,Test,2
    */
    }

    @Test
    public void saveTest() { //сперва мы проверяем loadFromFileTest(), если он работает корректно то с помощью него мы проверим метод save(), чтобы не использовать буфер/файлРидер
        Task task = new Task(10, "Task", "Test");
        Epic epic = new Epic(11, "Epic", "Test");
        Subtask subtask = new Subtask(12, "Subtask", "Test", 11);
        file = new File(".\\resources\\testTask.csv");
        managerRestored = new FileBackedTaskManager(file);
        managerRestored.tasks.put(task.getId(), task);
        managerRestored.epics.put(epic.getId(), epic);
        managerRestored.subtasks.put(subtask.getId(), subtask);
        managerRestored.save();
        managerRestored = FileBackedTaskManager.loadFromFile(file);
        assertTrue(new File(".\\resources\\testTask.csv").exists(), "save() не создал файл");
        assertNotNull(managerRestored.getTaskForId(task.getId()), "save() не сохранил Таски");
        assertNotNull(managerRestored.getEpicForId(epic.getId()), "save() не сохранил Епики");
        assertNotNull(managerRestored.getSubtasksForId(subtask.getId()), "save() не сохранил Субтаски");
    }
}
