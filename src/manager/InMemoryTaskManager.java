package manager;

import java.util.ArrayList;
import java.util.HashMap;

import task.Epic;
import task.Task;
import task.Subtask;
import task.Status;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public void addInMapTask(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addInMapEpic(Epic epic) {
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addInMapSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        } // Это если субтаск пытается субтаск сделать своим же Эпиком.
        subtask.setId(idCounter++);
        epic.getSubTaskIdList().add(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateStatusForEpic(epic);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTaskForId(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicForId(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtasksForId(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateStatusForEpic(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        if (!epic.getSubTaskIdList().isEmpty()) {
            for (Integer id : epic.getSubTaskIdList()) {
                if (subtasks.get(id).getStatus().equals(Status.NEW)) {
                    statusNew++;
                } else if (subtasks.get(id).getStatus().equals(Status.DONE)) {
                    statusDone++;
                }
            }
            if (statusNew == epic.getSubTaskIdList().size()) {
                epic.setStatus(Status.NEW);
            } else if (statusDone == epic.getSubTaskIdList().size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
            return;
        }
        epic.setStatus(Status.NEW);
    } // Сделал публичным, если делать приватным выходиит ошибка

    @Override
    public ArrayList<Subtask> getAllSubtaskForEpicId(int id) {
        ArrayList<Subtask> allSubtaskForEpicIdList = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubTaskIdList()) {
                allSubtaskForEpicIdList.add(subtasks.get(subtaskId));
            }
        }
        return allSubtaskForEpicIdList;
    }

    @Override
    public void removeAllTask() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTaskIdList().clear();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    @Override
    public void removeTaskForId(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicForId(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubTaskIdList()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    @Override
    public void removeSubtaskForId(int idSub) {
        if (subtasks.containsKey(idSub)) {
            Subtask sub = getSubtasksForId(idSub);
            Epic epi = getEpicForId(sub.getEpicId());
            epi.getSubTaskIdList().remove(Integer.valueOf(idSub));
            subtasks.remove(idSub);
            updateStatusForEpic(epi);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            task.setName(task.getName());
            task.setDescription(task.getDescription());
            task.setStatus(task.getStatus());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.setName(epic.getName());
            epic.setDescription(epic.getDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtask.setName(subtask.getName());
            subtask.setDescription(subtask.getDescription());
            subtask.setStatus(subtask.getStatus());
            updateStatusForEpic(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

}
