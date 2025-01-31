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
        assertEquals(1, inMemoryTaskManager.getTasks().size(), "InMemoryTaskManager Не сохраняет " +
                "Таски");
        assertEquals(1, inMemoryTaskManager.getEpics().size(), "InMemoryTaskManager Не сохраняет " +
                "Eпики");
        assertEquals(1, inMemoryTaskManager.getSubtasks().size(), "InMemoryTaskManager Не сохраняет " +
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
        inMemoryTaskManager.updateEpic(epic);
        assertEquals(epic.getStatus(), Status.DONE, "updateStatusForEpic работает не корректно");
        assertEquals(subtask.getStatus(), Status.DONE, "updateEpic работает не корректно");
        Subtask subtask2 = new Subtask("Тест 2", "Тест 2", epic.getId());
        inMemoryTaskManager.addInMapSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS,
                "updateStatusForEpic при добавление новой Субатаски работает не корректно");
    }

    @Test
    public void removeForIdTaskAndEpic() {
        inMemoryTaskManager.removeTaskForId(task.getId());
        assertEquals(0, inMemoryTaskManager.getTasks().size(),
                "inMemoryTaskManager не удаляет Таски по АЙди");
        inMemoryTaskManager.removeEpicForId(epic.getId());
        assertEquals(0, inMemoryTaskManager.getEpics().size(),
                "inMemoryTaskManager не удаляет Епик по Айди");
        assertEquals(0, inMemoryTaskManager.getSubtasks().size(),
                "Субтаски прикрепленные к Эпику не удаляются после удаления Эпика");
    }

    @Test
    public void removeForIdSubtask() {
        inMemoryTaskManager.removeSubtaskForId(subtask.getId());
        assertEquals(0, inMemoryTaskManager.getSubtasks().size(),
                "inMemoryTaskManager не удаляет Субтаски по Айди");
        assertEquals(0, inMemoryTaskManager.getEpicForId(epic.getId()).getSubTaskIdList().size(),
                "После метода удаления Субтаски, она не удалилась из списка Эпика которому принадлежит");
    }

    @Test
    public void removeAllTasksAndEpics() {
        inMemoryTaskManager.removeAllTask();
        assertEquals(0, inMemoryTaskManager.getTasks().size(),
                "inMemoryTaskManager не работает удаление всех Тасок");
        inMemoryTaskManager.removeAllEpics();
        assertEquals(0, inMemoryTaskManager.getEpics().size(),
                "inMemoryTaskManager не работает метод удаления всех Эпиков");
        assertEquals(0, inMemoryTaskManager.getSubtasks().size(),
                "После удаления всех Эпиков не произошло удаление всех субтасков");
    }

    @Test
    public void removeAllSubtasks() {
        inMemoryTaskManager.removeAllSubtasks();
        assertEquals(0, inMemoryTaskManager.getSubtasks().size(),
                "inMemoryTaskManager не работает метод на удаления всех Субтасков");
        assertEquals(0, inMemoryTaskManager.getEpicForId(epic.getId()).getSubTaskIdList().size(),
                "После метода удаления всех Субтасков, они не удалилась из списка Эпика которому принадлежат");
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
        assertEquals(1, inMemoryTaskManager.getSubtasks().size(), "Субтаск стал для Субтаска Эпиком");
    }

}
