package manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import task.Epic;
import task.Task;
import task.Subtask;
import task.Status;

public class InMemoryTaskManager implements TaskManager {
    protected int idCounter = 0;

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addInMapTask(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
        if (task.getStartTime() == null || !isTaskIntersection(task)) {
            return;
        }
        prioritizedTasks.add(task);
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
        if (subtask.getStartTime() == null || !isTaskIntersection(subtask)) {
            return;
        }
        prioritizedTasks.add(subtask);
        updateTimeForEpic(epic);
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
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicForId(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtasksForId(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
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
        Set<Integer> keys = tasks.keySet();
        for (Integer key : keys) {
            historyManager.remove(key);
        }
        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        Set<Integer> keys = epics.keySet();
        for (Integer key : keys) {
            historyManager.remove(key);
        }
        Set<Integer> keysSubtask = subtasks.keySet();
        for (Integer key : keysSubtask) {
            historyManager.remove(key);
        }
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        Set<Integer> keys = subtasks.keySet();
        for (Integer key : keys) {
            historyManager.remove(key);
        }
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        for (Epic epic : epics.values()) {
            epic.getSubTaskIdList().clear();
            epic.setStatus(Status.NEW);
            updateTimeForEpic(epic);
        }
        subtasks.clear();
    }

    @Override
    public void removeTaskForId(int id) {
        prioritizedTasks.remove(getTaskForId(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicForId(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getSubTaskIdList()) {
                prioritizedTasks.remove(getSubtasksForId(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtaskForId(int idSub) {
        prioritizedTasks.remove(getSubtasksForId(idSub));
        if (subtasks.containsKey(idSub)) {
            Subtask sub = getSubtasksForId(idSub);
            Epic epi = getEpicForId(sub.getEpicId());
            epi.getSubTaskIdList().remove(Integer.valueOf(idSub));
            subtasks.remove(idSub);
            updateStatusForEpic(epi);
            updateTimeForEpic(epi);
            historyManager.remove(idSub);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task.getStartTime() == null || !isTaskIntersection(task)) {
            return;
        }
        if (tasks.containsKey(task.getId())) {
            Task newTask = tasks.get(task.getId());
            newTask.setName(task.getName());
            newTask.setDescription(task.getDescription());
            newTask.setStatus(task.getStatus());
            newTask.setDuration(task.getDuration());
            newTask.setStartTime(task.getStartTime());
        }
    }

    @Override
    public void updateEpic(Epic epic) { //Эпика нет в трисете, так что здесь проверку не делал
        if (epics.containsKey(epic.getId())) {
            Task newEpic = epics.get(epic.getId());
            newEpic.setName(epic.getName());
            newEpic.setDescription(epic.getDescription());
            updateStatusForEpic(epic);
            updateTimeForEpic(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask.getStartTime() == null || !isTaskIntersection(subtask)) {
            return;
        }
        if (subtasks.containsKey(subtask.getId())) {
            Subtask newSubtask = subtasks.get(subtask.getId());
            newSubtask.setName(subtask.getName());
            newSubtask.setDescription(subtask.getDescription());
            newSubtask.setStatus(subtask.getStatus());
            newSubtask.setDuration(subtask.getDuration());
            newSubtask.setStartTime(subtask.getStartTime());
            updateStatusForEpic(epics.get(subtask.getEpicId()));
            updateTimeForEpic(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean isTaskIntersection(Task task) {
        if (getPrioritizedTasks().isEmpty()) {
            return true;
        }
        return getPrioritizedTasks().stream()
                .filter(newTask -> task.getStartTime().isBefore(newTask.getEndTime()) &&
                        task.getEndTime().isAfter(newTask.getStartTime()))
                .findAny()
                .isEmpty();
    }

    private void updateTimeForEpic(Epic epic) {
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        Duration totalDuration = Duration.ofMinutes(0);
        if (!epic.getSubTaskIdList().isEmpty()) {
            for (Integer id : epic.getSubTaskIdList()) {
                Subtask subtask = getSubtasksForId(id);
                totalDuration = totalDuration.plus(subtask.getDuration());
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
            epic.setStartTime(startTime);
            epic.setDuration(totalDuration);
            epic.setEndTime(endTime);
        } else {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        }
    }

    @Override
    public ArrayList<Integer> getSubtasksByEpic(Integer id) {
        Epic epic = epics.get(id);
        return epic.getSubTaskIdList();
    }

}
