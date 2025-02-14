package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVTaskFormatTest {
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUp() {
        task = new Task(1, "Task", "Test");
        epic = new Epic(2, "Epic", "Test");
        subtask = new Subtask(3, "Subtask", "Test", 2);
    }

    @Test
    public void toStringTaskTest() {
        assertEquals("1,TASK,Task,NEW,Test", CSVTaskFormat.toString(task),
                "toString() некорректно переводит Task в String");
        assertEquals("2,EPIC,Epic,NEW,Test", CSVTaskFormat.toString(epic),
                "toString() некорректно переводит Epic в String");
        assertEquals("3,SUBTASK,Subtask,NEW,Test,2", CSVTaskFormat.toString(subtask),
                "toString() некорректно переводит Subtask в String");
    }

    @Test
    public void taskFromStringTest() {
        assertEquals(task, CSVTaskFormat.taskFromString("1,TASK,Task,NEW,Test"),
                "taskFromString() некорректно переводит String в Task");
        assertEquals(epic, CSVTaskFormat.taskFromString("2,EPIC,Epic,NEW,Test"),
                "taskFromString() некорректно переводит String в Epic");
        assertEquals(subtask, CSVTaskFormat.taskFromString("3,SUBTASK,Subtask,NEW,Test,2"),
                "taskFromString() некорректно переводит String в Subtask");
    }
}
