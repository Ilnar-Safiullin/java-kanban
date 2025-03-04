package manager;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @Test
    public void testAddTaskEpicSubtaskAndChekIdGenerate() {
        assertEquals(1, taskManager.getTasks().size(), "InMemoryTaskManager Не сохраняет " +
                "Таски");
        assertEquals(1, taskManager.getEpics().size(), "InMemoryTaskManager Не сохраняет " +
                "Eпики");
        assertEquals(1, taskManager.getSubtasks().size(), "InMemoryTaskManager Не сохраняет " +
                "Субтаски");
        assertNotEquals(task.getId(), epic.getId(), "Не работает генератор Айди");
        assertNotEquals(task.getId(), subtask.getId(), "Не работает генератор Айди");
        assertNotEquals(epic.getId(), subtask.getId(), "Не работает генератор Айди");
    }

    @Test
    public void returnAllTaskEpicSubtaskAndReturnForIdTest() {
        assertNotNull(taskManager.getTasks(), "InMemoryTaskManager Не возвращает все Таски");
        assertNotNull(taskManager.getTaskForId(task.getId()), "InMemoryTaskManager Не возвращает Таски " +
                "по Айди");
        assertNotNull(taskManager.getEpics(), "InMemoryTaskManager Не возвращает все Epic");
        assertNotNull(taskManager.getEpicForId(epic.getId()), "InMemoryTaskManager Не возвращает Epic " +
                "по Айди");
        assertNotNull(taskManager.getSubtasks(), "InMemoryTaskManager Не возвращает все Subtask");
        assertNotNull(taskManager.getSubtasksForId(subtask.getId()), "InMemoryTaskManager Не возвращает " +
                "Subtask по Айди");
    }

    @Test
    public void testImmutabilityWhenAddingTheManager() {
        assertEquals(task.getName(), taskManager.getTaskForId(task.getId()).getName(),
                "Изменилось поле " + "name у Task после добавления в InMemoryTaskManager");
        assertEquals(task.getDescription(), taskManager.getTaskForId(task.getId()).getDescription(),
                "Изменилось поле Description у Task после добавления в InMemoryTaskManager");
        assertEquals(task.getStatus(), taskManager.getTaskForId(task.getId()).getStatus(),
                "Изменилось поле Status у Task после добавления в InMemoryTaskManager");
        assertEquals(epic.getName(), taskManager.getEpicForId(epic.getId()).getName(),
                "Изменилось поле " + "name у Epic после добавления в InMemoryTaskManager");
        assertEquals(epic.getDescription(), taskManager.getEpicForId(epic.getId()).getDescription(),
                "Изменилось поле Description у Epic после добавления в InMemoryTaskManager");
        assertEquals(epic.getStatus(), taskManager.getEpicForId(epic.getId()).getStatus(),
                "Изменилось поле Status у Epic после добавления в InMemoryTaskManager");
        assertEquals(subtask.getName(), taskManager.getSubtasksForId(subtask.getId()).getName(),
                "Изменилось поле " + "name у Subtask после добавления в InMemoryTaskManager");
        assertEquals(subtask.getDescription(), taskManager.getSubtasksForId(subtask.getId()).getDescription(),
                "Изменилось поле Description у Subtask после добавления в InMemoryTaskManager");
        assertEquals(subtask.getStatus(), taskManager.getSubtasksForId(subtask.getId()).getStatus(),
                "Изменилось поле Status у Subtask после добавления в InMemoryTaskManager");
    }

    @Test
    public void removeForIdTaskAndEpic() {
        taskManager.removeTaskForId(task.getId());
        assertEquals(0, taskManager.getTasks().size(),
                "inMemoryTaskManager не удаляет Таски по АЙди");
        taskManager.removeEpicForId(epic.getId());
        assertEquals(0, taskManager.getEpics().size(),
                "inMemoryTaskManager не удаляет Епик по Айди");
        assertEquals(0, taskManager.getSubtasks().size(),
                "Субтаски прикрепленные к Эпику не удаляются после удаления Эпика");
    }

    @Test
    public void removeForIdSubtask() {
        taskManager.removeSubtaskForId(subtask.getId());
        assertEquals(0, taskManager.getSubtasks().size(),
                "inMemoryTaskManager не удаляет Субтаски по Айди");
        assertEquals(0, taskManager.getEpicForId(epic.getId()).getSubTaskIdList().size(),
                "После метода удаления Субтаски, она не удалилась из списка Эпика которому принадлежит");
    }

    @Test
    public void removeAllTasksAndEpics() {
        taskManager.removeAllTask();
        assertEquals(0, taskManager.getTasks().size(),
                "inMemoryTaskManager не работает удаление всех Тасок");
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getEpics().size(),
                "inMemoryTaskManager не работает метод удаления всех Эпиков");
        assertEquals(0, taskManager.getSubtasks().size(),
                "После удаления всех Эпиков не произошло удаление всех субтасков");
    }

    @Test
    public void removeAllSubtasks() {
        taskManager.removeAllSubtasks();
        assertEquals(0, taskManager.getSubtasks().size(),
                "inMemoryTaskManager не работает метод на удаления всех Субтасков");
        assertEquals(0, taskManager.getEpicForId(epic.getId()).getSubTaskIdList().size(),
                "После метода удаления всех Субтасков, они не удалилась из списка Эпика которому принадлежат");
    }

    @Test
    public void getHistory() {
        taskManager.getTaskForId(task.getId());
        taskManager.getEpicForId(epic.getId());
        taskManager.getSubtasksForId(subtask.getId());
        assertEquals(3, taskManager.getHistory().size(),
                "Метод getHistory в InMemoryTaskManager работает не корректно");
    }

    @Test
    public void notSubtaskMakeYourEpic() {
        Subtask subtask2 = new Subtask("Тест", "Тест", subtask.getId(), Duration.ofMinutes(10),
                LocalDateTime.now().minusMinutes(15L));
        taskManager.addInMapSubtask(subtask2);
        assertEquals(1, taskManager.getSubtasks().size(), "Субтаск стал для Субтаска Эпиком");
    }

    @Test
    public void updateEpicStatusTest() {
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epic);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "updateStatusForEpic работает не корректно");
        subtask.setStatus(Status.DONE);
        taskManager.updateEpic(epic);
        assertEquals(epic.getStatus(), Status.DONE, "updateStatusForEpic работает не корректно");
        assertEquals(subtask.getStatus(), Status.DONE, "updateEpic работает не корректно");
        Subtask subtask2 = new Subtask("Тест 2", "Тест 2", epic.getId(), Duration.ofMinutes(10),
                LocalDateTime.now().minusMinutes(40L));
        taskManager.addInMapSubtask(subtask2);
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
        taskManager.addInMapTask(task2);
        assertEquals(taskManager.getPrioritizedTasks().size(), 2, "Добавляет задачи с пересечением по времени");
    }

    @Test
    public void updateTest() {
        Task task2 = new Task(task.getId(), "newTask", "new", Duration.ofMinutes(10),
                LocalDateTime.now().minusMinutes(80L));
        Epic epic2 = new Epic(epic.getId(), "newEpic", "new");
        Subtask subtask2 = new Subtask(subtask.getId(), "new", "new", epic.getId(),
                Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(120L));
        taskManager.updateTask(task2);
        taskManager.updateEpic(epic2);
        taskManager.updateSubtask(subtask2);
        assertEquals(task.getName(), task2.getName(), "не обновляет Таски");
        assertEquals(epic.getName(), epic2.getName(), "не обновляет Эпики");
        assertEquals(subtask.getName(), subtask2.getName(), "не обновляет Субтаски");
    }

    @Test
    public void prioritizedTasksTest() {
        Task task3 = new Task("Task 3", "Third Task", Duration.ofMinutes(10),
                LocalDateTime.of(2023, 10, 1, 9, 0));
        taskManager.addInMapTask(task3);
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(3, prioritizedTasks.size(), "Количество задач не соответствует ожидаемому");
        assertEquals(task3, prioritizedTasks.get(0), "Первая задача должна быть task3");
        assertEquals(subtask, prioritizedTasks.get(1), "Вторая задача должна быть subtask");
        assertEquals(task, prioritizedTasks.get(2), "Третья задача должна быть task");
    }

    @Test
    public void prioritizedAddIntersectionTest() {
        Task task2 = new Task("Task 2", "Second Task", Duration.ofMinutes(10), LocalDateTime.now()); // Пересекается с task
        taskManager.addInMapTask(task2);
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritizedTasks.size(), "Должна остаться только 2 задачи из-за пересечения");
    }
}