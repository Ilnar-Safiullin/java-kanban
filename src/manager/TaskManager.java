package manager;

import java.util.ArrayList;
import java.util.Set;

import task.Epic;
import task.Task;
import task.Subtask;

public interface TaskManager {

    void addInMapTask(Task task);

    void addInMapEpic(Epic epic);

    void addInMapSubtask(Subtask subtask);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    Task getTaskForId(int id);

    Epic getEpicForId(int id);

    Subtask getSubtasksForId(int id);

    ArrayList<Subtask> getAllSubtaskForEpicId(int id);

    void removeAllTask();

    void removeAllEpics();

    void removeAllSubtasks();

    void removeTaskForId(int id);

    void removeEpicForId(int id);

    void removeSubtaskForId(int idSub);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    ArrayList<Task> getHistory();

    Set<Task> getPrioritizedTasks();

}
