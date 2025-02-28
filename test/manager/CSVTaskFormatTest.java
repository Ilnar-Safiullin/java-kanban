package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVTaskFormatTest {
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUp() {
        task = new Task(1, "Task", "Test", Duration.ofMinutes(10), LocalDateTime.of(1, 1, 1, 1, 1));
        epic = new Epic(2, "Epic", "Test");
        subtask = new Subtask(3, "Subtask", "Test", 2, Duration.ofMinutes(10), LocalDateTime.of(2, 2, 2, 2, 2));
    }

    @Test
    public void toStringTaskTest() {
        assertEquals("1,TASK,Task,NEW,Test,10,0001-01-01T01:01", CSVTaskFormat.toString(task),
                "toString() некорректно переводит Task в String");
        assertEquals("2,EPIC,Epic,NEW,Test,0,null", CSVTaskFormat.toString(epic),
                "toString() некорректно переводит Epic в String");
        assertEquals("3,SUBTASK,Subtask,NEW,Test,10,0002-02-02T02:02,2", CSVTaskFormat.toString(subtask),
                "toString() некорректно переводит Subtask в String");
    }

    @Test
    public void taskFromStringTest() {
        assertEquals(task, CSVTaskFormat.taskFromString("1,TASK,Task,NEW,Test,10,0001-01-01T01:01"),
                "taskFromString() некорректно переводит String в Task");
        assertEquals(epic, CSVTaskFormat.taskFromString("2,EPIC,Epic,NEW,Test,0,3099-01-01T00:00"),
                "taskFromString() некорректно переводит String в Epic");
        assertEquals(subtask, CSVTaskFormat.taskFromString("3,SUBTASK,Subtask,NEW,Test,10,0002-02-02T02:02,2"),
                "taskFromString() некорректно переводит String в Subtask");
    }
}
