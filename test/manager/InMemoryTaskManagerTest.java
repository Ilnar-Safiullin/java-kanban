package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void setUP() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task = new Task("Test", "Test Task");
        inMemoryTaskManager.addInMapTask(task);
        epic = new Epic("Test", "Test Epic");
        inMemoryTaskManager.addInMapEpic(epic);
        subtask = new Subtask("Test", "Test Subtask", epic.getId());
        inMemoryTaskManager.addInMapSubtask(subtask);
    }

    @Test
    public void testAddTaskEpicSubtaskAndChekIdGenerate() {
        for (int i = 0; i < 5; i++) {
            inMemoryTaskManager.addInMapTask(task);
            inMemoryTaskManager.addInMapEpic(epic);
            inMemoryTaskManager.addInMapSubtask(subtask);
        }
        assertEquals(6, inMemoryTaskManager.getTasks().size(), "InMemoryTaskManager Не сохраняет " +
                "Таски");
        assertEquals(6, inMemoryTaskManager.getEpics().size(), "InMemoryTaskManager Не сохраняет " +
                "Eпики");
        assertEquals(6, inMemoryTaskManager.getSubtasks().size(), "InMemoryTaskManager Не сохраняет " +
                "Субтаски");
        assertNotEquals(task.getId(), epic.getId(), "Не работает генератор Айди");
        assertNotEquals(task.getId(), subtask.getId(), "Не работает генератор Айди");
        assertNotEquals(epic.getId(), subtask.getId(), "Не работает генератор Айди");
    }

    @Test
    public void returnAllTaskEpicSubtaskAndReturnForIdTest() {
        assertNotNull(inMemoryTaskManager.getTasks(), "InMemoryTaskManager Не возвращает все Таски");
        assertNotNull(inMemoryTaskManager.getTaskForId(task.getId()), "InMemoryTaskManager Не возвращает Таски " +
                "по Айди");
        assertNotNull(inMemoryTaskManager.getEpics(), "InMemoryTaskManager Не возвращает все Epic");
        assertNotNull(inMemoryTaskManager.getEpicForId(epic.getId()), "InMemoryTaskManager Не возвращает Epic " +
                "по Айди");
        assertNotNull(inMemoryTaskManager.getTasks(), "InMemoryTaskManager Не возвращает все Таски");
        assertNotNull(inMemoryTaskManager.getTaskForId(task.getId()), "InMemoryTaskManager Не возвращает Таски " +
                "по Айди");
        assertNotNull(inMemoryTaskManager.getSubtasks(), "InMemoryTaskManager Не возвращает все Subtask");
        assertNotNull(inMemoryTaskManager.getSubtasksForId(subtask.getId()), "InMemoryTaskManager Не возвращает " +
                "Subtask по Айди");
    }

    @Test
    public void testImmutabilityWhenAddingTheManager() {
        assertEquals(task.getName(), inMemoryTaskManager.getTaskForId(task.getId()).getName(),
                "Изменилось поле " + "name у Task после добавления в InMemoryTaskManager");
        assertEquals(task.getDescription(), inMemoryTaskManager.getTaskForId(task.getId()).getDescription(),
                "Изменилось поле Description у Task после добавления в InMemoryTaskManager");
        assertEquals(task.getStatus(), inMemoryTaskManager.getTaskForId(task.getId()).getStatus(),
                "Изменилось поле Status у Task после добавления в InMemoryTaskManager");
        assertEquals(epic.getName(), inMemoryTaskManager.getEpicForId(epic.getId()).getName(),
                "Изменилось поле " + "name у Epic после добавления в InMemoryTaskManager");
        assertEquals(epic.getDescription(), inMemoryTaskManager.getEpicForId(epic.getId()).getDescription(),
                "Изменилось поле Description у Epic после добавления в InMemoryTaskManager");
        assertEquals(epic.getStatus(), inMemoryTaskManager.getEpicForId(epic.getId()).getStatus(),
                "Изменилось поле Status у Epic после добавления в InMemoryTaskManager");
        assertEquals(subtask.getName(), inMemoryTaskManager.getSubtasksForId(subtask.getId()).getName(),
                "Изменилось поле " + "name у Subtask после добавления в InMemoryTaskManager");
        assertEquals(subtask.getDescription(), inMemoryTaskManager.getSubtasksForId(subtask.getId()).getDescription(),
                "Изменилось поле Description у Subtask после добавления в InMemoryTaskManager");
        assertEquals(subtask.getStatus(), inMemoryTaskManager.getSubtasksForId(subtask.getId()).getStatus(),
                "Изменилось поле Status у Subtask после добавления в InMemoryTaskManager");
    }

    @Test
    public void updateEpicStatusTest() {
        subtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "updateStatusForEpic работает не корректно");
        assertEquals(subtask.getStatus(), Status.IN_PROGRESS, "updateSubtask работает не корректно");
        subtask.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(subtask);
        assertEquals(epic.getStatus(), Status.DONE, "updateStatusForEpic работает не корректно");
        assertEquals(subtask.getStatus(), Status.DONE, "updateSubtask работает не корректно");
        Subtask subtask2 = new Subtask("Тест 2", "Тест 2", epic.getId());
        inMemoryTaskManager.addInMapSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS,
                "updateStatusForEpic при добавление новой Субатаски работает не корректно");
    }

    @Test
    public void removeTask() {
        for (int i = 0; i < 5; i++) {
            inMemoryTaskManager.addInMapTask(task);
        }
        inMemoryTaskManager.removeTaskForId(task.getId());
        assertEquals(5, inMemoryTaskManager.getTasks().size(),
                "inMemoryTaskManager не удаляет Таски по АЙди");
        inMemoryTaskManager.removeAllTask();
        assertEquals(0, inMemoryTaskManager.getTasks().size(),
                "inMemoryTaskManager не удаляет все Таски");
    }

    @Test
    public void removeEpic() {
        for (int i = 0; i < 5; i++) {
            inMemoryTaskManager.addInMapEpic(epic);
            inMemoryTaskManager.addInMapSubtask(subtask);
        }
        inMemoryTaskManager.removeEpicForId(epic.getId());
        assertEquals(5, inMemoryTaskManager.getEpics().size(),
                "inMemoryTaskManager не удаляет Епик по Айди");
        assertEquals(0, inMemoryTaskManager.getSubtasks().size(),
                "Субтаски прикрепленные к Эпику не удаляются после удаления Эпика");
        inMemoryTaskManager.removeAllEpics();
        assertEquals(0, inMemoryTaskManager.getEpics().size(),
                "inMemoryTaskManager не работает метод удаления всех Эпиков");
        assertEquals(0, inMemoryTaskManager.getSubtasks().size(),
                "После удаления всех Эпиков не произошло удаление всех субтасков");
    }

    @Test
    public void removeSubtask() {
        for (int i = 0; i < 5; i++) {
            inMemoryTaskManager.addInMapSubtask(subtask);
        }
        inMemoryTaskManager.removeSubtaskForId(subtask.getId());
        assertEquals(5, inMemoryTaskManager.getSubtasks().size(),
                "inMemoryTaskManager не удаляет Субтаски по Айди");
        assertEquals(5, inMemoryTaskManager.getEpicForId(epic.getId()).getSubTaskIdList().size(),
                "После метода удаления Субтаски, она не удалилась из списка Эпика которому принадлежит");
        inMemoryTaskManager.removeAllSubtasks();
        assertEquals(0, inMemoryTaskManager.getSubtasks().size(),
                "inMemoryTaskManager не работает метод на удаления всех Субтасков");
        assertEquals(0, inMemoryTaskManager.getEpicForId(epic.getId()).getSubTaskIdList().size(),
                "После метода удаления всех Субтасков, они не удалилась из списка Эпика которому принадлежат");
    }

    @Test
    public void checkTasksWithTheGivenIdAndTheGeneratedIdDoNotConflict() {
        Task task2 = new Task("Тест", "Test");
        task2.setId(task.getId());
        inMemoryTaskManager.addInMapTask(task2);
        assertEquals(2, inMemoryTaskManager.getTasks().size(),
                "Task с вручную присвоенным номером АЙди(равным уже существующему Таску) не добавился");
        Epic epic2 = new Epic("Тест", "Test");
        epic2.setId(epic.getId());
        inMemoryTaskManager.addInMapEpic(epic2);
        assertEquals(2, inMemoryTaskManager.getEpics().size(),
                "Epic с вручную присвоенным номером АЙди(равным уже существующему Epic) не добавился");
        Subtask subtask2 = new Subtask("Тест", "Test", epic.getId());
        subtask2.setId(subtask.getId());
        inMemoryTaskManager.addInMapSubtask(subtask2);
        assertEquals(2, inMemoryTaskManager.getSubtasks().size(),
                "Subtask с вручную присвоенным номером АЙди не добавилась");
        assertEquals(2, inMemoryTaskManager.getEpicForId(epic.getId()).getSubTaskIdList().size(),
                "Subtask с вручную присвоенным номером Айди(равным уже существующему Subtask) не добавился " +
                        "в список внутри Epic");
    }

    @Test
    public void getHistory() {
        inMemoryTaskManager.getTaskForId(task.getId());
        inMemoryTaskManager.getEpicForId(epic.getId());
        inMemoryTaskManager.getSubtasksForId(subtask.getId());
        assertEquals(3, inMemoryTaskManager.getHistory().size(),
                "Метод getHistory в InMemoryTaskManager работает не корректно");
    }

    @Test
    public void notSubtaskMakeYourEpic() {
        Subtask subtask2 = new Subtask("Тест", "Тест", subtask.getId());
        inMemoryTaskManager.addInMapSubtask(subtask2);
        assertNull(inMemoryTaskManager.getSubtasksForId(subtask2.getId()), "Субтаск стал для Субтаска Эпиком");
    }

}
