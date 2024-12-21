import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCounter = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void newTask(String name, String description) { //Автоматическое создание и добавление в мапу. Но наверное эти методы лищние (newTask, newEpic, newSubtask)
        Task task = new Task(name, description);
        generateIdTask(task);
        addInMapTask(task);
    }

    public void newEpic(String name, String description) { // С тасками автомат было просто делать, а тут тяжело уже потом после такого создания Епика, прикрепить к ней Субтаску
        Epic epic = new Epic(name, description);
        generateIdEpic(epic);
        addInMapEpic(epic);
    }

    public void newSubtask(String name, String description) { // Аналогично как у Епиков. Создаешь субтаску, а вот как Айди Епика в поле epicId субтаски прикрутить не придумал. И вроде как лишние эти 3 метода.
        Subtask subtask = new Subtask(name, description);
        generateIdSubtask(subtask);
        addInMapSubtask(subtask);
    }

    public void generateIdSubtask(Subtask subtask) {
        subtask.id = idCounter;
        idCounter++;
    }

    public void generateIdEpic(Epic epic) {
        epic.id = idCounter;
        idCounter++;
    }

    public void generateIdTask(Task task) {
        task.id = idCounter;
        idCounter++;
    }

    public void addInMapTask(Task task) {
        generateIdTask(task); // если таски через newTask создавать то эта строчка тут будет лишней
        tasks.put(task.getId(), task);
    }

    public void addInMapEpic(Epic epic) {
        generateIdEpic(epic);
        epics.put(epic.getId(), epic);
    }

    public void addInMapSubtask(Subtask subtask) {
        generateIdSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksArrayList = new ArrayList<>(tasks.values());
        return tasksArrayList;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsArrayList = new ArrayList<>(epics.values());
        return epicsArrayList;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksArrayList = new ArrayList<>(subtasks.values());
        return subtasksArrayList;
    }

    public Task searchForIdTask(int id) {
        if (tasks.containsKey(id)) { // Везде прикрутил проверку на containsKey. Ато если дать ему параметром id с которым задачи у нас нет, то выходила ошибка. Или это лищние?
            return tasks.get(id);
        }
        return null;
    }

    public Epic searchForIdEpic(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        }
        return null;
    }

    public Subtask searchForIdSubtasks(int id) {
        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        }
        return null;
    }

    public void addSubtaskIdInEpicId(Subtask subtask, int epicId) { //вот у этого и ниже метода не придумал как один Аргумент оставить вместо двух. И нижний лучше чем этот думаю
        Epic epi = epics.get(epicId);
        epi.subTaskId.add(subtask.getId());
        subtask.epicId = epicId;
    }

    public void addSubtaskIdInEpic(Subtask subtask, Epic epic) {
        epic.subTaskId.add(subtask.getId());
        subtask.epicId = epic.id;
    }

    public void changeStatusForIdTask(int id, Status status) {
        if (tasks.containsKey(id)) {
            Task tas = tasks.get(id);
            tas.setStatus(status);
        }
    }

    public void changeStatusForIdEpic(int id, Status status) {
        if (epics.containsKey(id)) {
            Epic epi = epics.get(id);
            epi.setStatus(status);
        }
    }

    public void changeStatusForIdSubtask(int id, Status status) {
        if (subtasks.containsKey(id)) {
            Subtask sub = subtasks.get(id);
            sub.setStatus(status);
            chekSubStatusAndChangeEpicStatus();
        }
    }

    public void chekSubStatusAndChangeEpicStatus() {
        for (Epic epic : epics.values()) {
            if (!epic.subTaskId.isEmpty()) {
                for (Status status : Status.values()) {
                    boolean sameStatus = true;
                    for (int i : epic.subTaskId) {
                        Subtask sub = searchForIdSubtasks(i);
                        if (sub == null) {
                            sameStatus = false;
                            break;
                        }
                        if (!sub.status.equals(status)) {
                            sameStatus = false;
                            break;
                        }
                    }
                    if (sameStatus) {
                        epic.status = status;
                    }
                }
            }
        }
    }

    public ArrayList<Subtask> getAllSubtaskForEpicId(int id) {
        ArrayList<Subtask> allSubtaskForEpicIdList = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic epic = searchForIdEpic(id);
            if (!(epic == null)) {
                for (int i : epic.subTaskId) {
                    allSubtaskForEpicIdList.add(searchForIdSubtasks(i));
                }
                return allSubtaskForEpicIdList;
            }
        }
        return null;
    }

    public void removeAllTask() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        removeAllSubtasks();
    }

    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            if (!epic.subTaskId.isEmpty()) {
                epic.subTaskId.clear();
            }
        }
        subtasks.clear();
    }

    public void removeTaskForId(int id) {

        tasks.remove(id);
    }

    public void removeEpicsForId(int id) {
        if (epics.containsKey(id)) {
            Epic epic = searchForIdEpic(id);
            ArrayList<Integer> subtaskId = new ArrayList<>();
            for (int i : epic.subTaskId) {
                subtaskId.add(i);
            }
            epics.remove(id);
            for (int j : subtaskId) {
                removeSubtasksForId(j);
            }
        }
    }

    public void removeSubtasksForId(int idSub) {
        if (subtasks.containsKey(idSub)) {
            Subtask sub = searchForIdSubtasks(idSub);
            Epic epi = searchForIdEpic(sub.getEpicId());
            if (epi == null) {
                subtasks.remove(idSub);
                return;
            }
            epi.subTaskId.removeIf(id -> id == idSub); // epi.subTaskId.remove(id) - удалял по индексу а не по значению, нашел замену в интернете
            subtasks.remove(idSub);
            chekSubStatusAndChangeEpicStatus();
        }
    }

    public ArrayList<Subtask> searchAllSubtaskInEpicId(int id) {
        if (epics.containsKey(id)) {
            Epic epic = searchForIdEpic(id);
            if (!(epic == null)) {
                ArrayList<Subtask> subtaskForEpicList = new ArrayList<>();
                for (int subId : epic.subTaskId) {
                    subtaskForEpicList.add(searchForIdSubtasks(subId));
                }
                return subtaskForEpicList;
            }
        }
        return null;
    }

}
