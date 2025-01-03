package manager;

import java.util.ArrayList;
import java.util.HashMap;

import task.Epic;
import task.Task;
import task.Subtask;
import task.Status;

public class TaskManager {
    private int idCounter = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void addInMapTask(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
    }

    public void addInMapEpic(Epic epic) {
        epic.setId(idCounter++);
        epics.put(epic.getId(), epic);
    }

    public void addInMapSubtask(Subtask subtask) {
        subtask.setId(idCounter++);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTaskIdList().add(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateStatusForEpic(epic);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Task searchForIdTask(int id) {
        return tasks.get(id);
    }

    public Epic searchForIdEpic(int id) {
        return epics.get(id);
    }

    public Subtask searchForIdSubtasks(int id) {
        return subtasks.get(id);
    }

    private void updateStatusForEpic(Epic epic) {
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
    }

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

    public void removeAllTask() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTaskIdList().clear();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    public void removeTaskForId(int id) {
        tasks.remove(id);
    }

    public void removeEpicForId(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubTaskIdList()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    public void removeSubtaskForId(int idSub) {
        if (subtasks.containsKey(idSub)) {
            Subtask sub = searchForIdSubtasks(idSub);
            Epic epi = searchForIdEpic(sub.getEpicId());
            epi.getSubTaskIdList().remove(Integer.valueOf(idSub));
            subtasks.remove(idSub);
            updateStatusForEpic(epi);
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
           task.setName(task.getName());
           task.setDescription(task.getDescription());
           task.setStatus(task.getStatus());
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.setName(epic.getName());
            epic.setDescription(epic.getDescription());
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtask.setName(subtask.getName());
            subtask.setDescription(subtask.getDescription());
            subtask.setStatus(subtask.getStatus());
            updateStatusForEpic(epics.get(subtask.getEpicId()));
        }
    }

}
