import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, ArrayList<Task>> tasks;
    HashMap<Integer, ArrayList<Epic>> epics;
    HashMap<Integer, ArrayList<Subtask>> subtasks;

    public TaskManager() {
        epics = new HashMap<>();
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public void addInMapEpic(int id, Epic epic) {
        if (!epics.containsKey(id)) {
            epics.put(id, new ArrayList<>());
        }
        epics.get(id).add(epic);
    }

    public void addInMapTask(int q, Task task) {
        if (!tasks.containsKey(q)) {
            tasks.put(q, new ArrayList<>());
        }
        tasks.get(q).add(task);
    }

    public void addInMapSubtask(int q, Subtask subtask) {
        if (!subtasks.containsKey(q)) {
            subtasks.put(q, new ArrayList<>());
        }
        subtasks.get(q).add(subtask);
    }

    public void printAllTasks() {
        System.out.println("Список задач: Описание, Статус, Id, epicId(если есть) ");
        System.out.println();
        if (!tasks.isEmpty()) {
            System.out.println("Список тасков:");
            for (ArrayList<Task> taskList : tasks.values()) {
                for (Task task : taskList) {
                    System.out.println(task.getDescription() + ", Статус: " + task.getStatus() + ", ID: " +
                            task.getId());
                }
            }
        }
        System.out.println();
        if (!epics.isEmpty()) {
            System.out.println("Список эпиков:");
            for (ArrayList<Epic> epicList : epics.values()) {
                for (Epic epic : epicList) {
                    System.out.println(epic.getDescription() + ", Статус: " + epic.getStatus() + ", ID: " +
                            epic.getId() + ", Epic ID: " + epic.getEpicId());
                }
            }
        }
        System.out.println();
        if (!subtasks.isEmpty()) {
            System.out.println("Список сабтасков:");
            for (ArrayList<Subtask> subtaskList : subtasks.values()) {
                for (Subtask subtask : subtaskList) {
                    System.out.println(subtask.getDescription() + ", Статус: " + subtask.getStatus() + ", ID: " +
                            subtask.getId() + ", Epic ID: " + subtask.getEpicId());
                }
            }
            System.out.println();
        }
        System.out.println("---------------");
    }

    public void searchForId(int id) {
        System.out.println("Поиск задачи по id: " + id);
        if (!tasks.isEmpty()) {
            for (ArrayList<Task> taskList : tasks.values()) {
                for (Task task : taskList) {
                    if (task.getId() == id) {
                        System.out.println("Задача найдена");
                        System.out.println("Вид: Task, " + task.getDescription() + ", Статус: " + task.getStatus() + ", ID: " +
                                task.getId());
                        System.out.println();
                        return;
                    }
                }
            }
        }
        if (!epics.isEmpty()) {
            for (ArrayList<Epic> epicList : epics.values()) {
                for (Epic epic : epicList) {
                    if (epic.getId() == id) {
                        System.out.println("Задача найдена");
                        System.out.println("Вид: Epic, " + epic.getDescription() + ", Статус: " + epic.getStatus() +
                                ", ID: " + epic.getId() + ", Epic ID: " + epic.getEpicId());
                        System.out.println();
                        return;
                    }
                }
            }
        }
        if (!subtasks.isEmpty()) {
            for (ArrayList<Subtask> subtaskList : subtasks.values()) {
                for (Subtask subtask : subtaskList) {
                    if (subtask.getId() == id) {
                        System.out.println("Задача найдена");
                        System.out.println("Вид: Subtask, " + subtask.getDescription() + ", Статус: " +
                                subtask.getStatus() + ", ID: " + subtask.getId() +
                                ", привязка к Епику (epicId): " + subtask.getEpicId());
                        System.out.println();
                        return;
                    }
                }
            }
        }
        System.out.println("Задача с таким id не найдена, повторите ввод");
        System.out.println();
    }

    public void changeStatusForId(int id, Status status) {
        System.out.println("Поменять статус у задачи с id: " + id);
        if (!tasks.isEmpty()) {
            for (ArrayList<Task> taskList : tasks.values()) {
                for (Task task : taskList) {
                    if (task.getId() == id) {
                        System.out.println("Задача найдена");
                        System.out.println("Вид: Task, " + task.getDescription() + ", Статус: " +
                                task.getStatus() + ", ID: " + task.getId());
                        task.setStatus(status);
                        System.out.println("Внимание смена статуса! Новый статус задачи: " + task.getStatus());
                        System.out.println();
                        return;
                    }
                }
            }
        }
        if (!epics.isEmpty()) {
            for (ArrayList<Epic> epicList : epics.values()) {
                for (Epic epic : epicList) {
                    if (epic.getId() == id) {
                        System.out.println("Задача найдена");
                        System.out.println("Вид: Epic, " + epic.getDescription() + ", Статус: " + epic.getStatus() +
                                ", ID: " + epic.getId() + ", Epic ID: " + epic.getEpicId());
                        epic.setStatus(status);
                        System.out.println("Внимание смена статуса! Новый статус задачи: " + epic.getStatus());
                        System.out.println();
                        return;
                    }
                }
            }
        }
        if (!subtasks.isEmpty()) {
            for (ArrayList<Subtask> subtaskList : subtasks.values()) {
                for (Subtask subtask : subtaskList) {
                    if (subtask.getId() == id) {
                        System.out.println("Задача найдена");
                        System.out.println("Вид: Subtask, " + subtask.getDescription() + ", Статус: " +
                                subtask.getStatus() + ", ID: " + subtask.getId() +
                                ", привязка к Епику (epicId): " + subtask.getEpicId());
                        subtask.setStatus(status);
                        System.out.println("Внимание смена статуса! Новый статус задачи: " + subtask.getStatus());
                        System.out.println();
                        return;
                    }
                }
            }
        }
        System.out.println("Задача с таким id не найдена, повторите ввод");
        System.out.println();
    }

    public void chekSubStatusAndChangeEpicStatus(int epicId) {
        boolean correct = false; // добавил эту переменную, так как если вводить число в ЕпикАйди большое (с которым нет такой задачи) выходил не корректный вывод.
        for (Status status : Status.values()) {
            boolean sameStatus = true;
            if (!subtasks.isEmpty()) {
                for (ArrayList<Subtask> subtaskList : subtasks.values()) {
                    for (Subtask subtask : subtaskList) {
                        if (subtask.getEpicId() == epicId) {
                            correct = true;
                            if (!subtask.getStatus().equals(status)) {
                                sameStatus = false;
                                break;
                            }
                        }
                    }
                    if (!sameStatus) {
                        break;
                    }
                }
            }
            if (correct) {
                if (sameStatus) {
                    if (!epics.isEmpty()) {
                        for (ArrayList<Epic> epicList : epics.values()) {
                            for (Epic epic : epicList) {
                                if (epic.getEpicId() == epicId) {
                                    if (!epic.getStatus().equals(status)) {
                                        System.out.println("Все подзадачи Эпика <" + epic.getDescription() +
                                                ">  сменили статус");
                                        System.out.println("Установлен новый статус: " + status);
                                        System.out.println();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void addSubtask(Epic epic, Subtask subtask) {
        subtask.setEpicId(epic.getEpicId());
    }

    public void removeAllTask() {
        System.out.println("Удаляем все задачи");
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void removeTaskForId(int id) {
        System.out.println("Удаление задачи по номеру id: " + id);
        if (!tasks.isEmpty()) {
            for (ArrayList<Task> taskList : tasks.values()) {
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getId() == id) {
                        System.out.println("Задача <" + taskList.get(i).getDescription() + "> удалена");
                        taskList.remove(i);
                        System.out.println();
                        return;
                    }
                }
            }
        }
        if (!epics.isEmpty()) {
            for (ArrayList<Epic> epicList : epics.values()) {
                for (int i = 0; i < epicList.size(); i++) {
                    if (epicList.get(i).getEpicId() == id) {
                        System.out.println("Задача <" + epicList.get(i).getDescription() + "> удалена");
                        epicList.remove(i);
                        System.out.println();
                        return;
                    }
                }
            }
        }
        if (!subtasks.isEmpty()) {
            for (ArrayList<Subtask> subtaskList : subtasks.values()) {
                for (int i = 0; i < subtaskList.size(); i++) {
                    if (subtaskList.get(i).getId() == id) {
                        System.out.println("Задача <" + subtaskList.get(i).getDescription() + "> удалена");
                        subtaskList.remove(i);
                        System.out.println();
                        return;
                    }
                }
            }
        }
        System.out.println("Задача с таким id не найдена, повторите ввод");
        System.out.println();
    }

    public void searchForSubtaskForEpicId(int epicId) {
        System.out.println("Ищем все подзадачи для Эпика (epicId): " + epicId);
        boolean correct = false; // добавил эту переменную, так как если вводить число в ЕпикАйди большое (с которым нет такой задачи) выходил не корректный вывод.
        ArrayList<Subtask> subtaskEpic = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (ArrayList<Subtask> subtaskList : subtasks.values()) {
                for (Subtask subtask : subtaskList) {
                    if (subtask.getEpicId() == epicId) {
                        correct = true;
                        subtaskEpic.add(subtask);
                    }
                }
            }
        }
        if (correct) {
            if (!epics.isEmpty()) {
                System.out.println("Задачи найдены: ");
                for (Subtask subtask : subtaskEpic) {
                    System.out.println(subtask.getDescription() + ", статус: " + subtask.getStatus() + ", id " +
                            subtask.getId() + ", epicId: " + subtask.getEpicId());
                }
                System.out.println();
                return;
            }
        }
        System.out.println("У данного Епика нет подзадач");
    }

}