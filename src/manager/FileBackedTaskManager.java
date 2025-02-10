package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    static File file;
    private static Integer maxIdNumber = 0;
    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        FileBackedTaskManager managerRestored = Managers.getDefault();
        managerRestored.loadFromFile(file);
        System.out.println(managerRestored.inMemoryTaskManager.getTasks());
        System.out.println(managerRestored.inMemoryTaskManager.getEpics());
        System.out.println(managerRestored.inMemoryTaskManager.getSubtasks());
        System.out.println(managerRestored.inMemoryTaskManager.getHistory());
    }

    public static void loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                boolean b = data.length > 0;
                if (data.length > 1) {
                    if (data[1].equals(TaskType.TASK.toString())) {
                        Task task = CSVTaskFormat.taskFromString(line);
                        addTask(task);
                        if (Integer.parseInt(data[0]) > maxIdNumber) {
                            maxIdNumber = Integer.parseInt(data[0]);
                        }
                    } else if (data[1].equals(TaskType.EPIC.toString())) {
                        Epic epic = (Epic) CSVTaskFormat.taskFromString(line);
                        addTask(epic);
                        if (Integer.parseInt(data[0]) > maxIdNumber) {
                            maxIdNumber = Integer.parseInt(data[0]);
                        }
                    } else if (data[1].equals(TaskType.SUBTASK.toString())) {
                        Subtask subtask = (Subtask) CSVTaskFormat.taskFromString(line);
                        addTask(subtask);
                        if (Integer.parseInt(data[0]) > maxIdNumber) {
                            maxIdNumber = Integer.parseInt(data[0]);
                        }
                    } else if (data[1].equals("type")) {
                        continue;
                    } else {
                        for (int i = 0; i < data.length; i++) {
                            if (inMemoryTaskManager.taskContainsKey(Integer.parseInt(data[i]))) {
                                addHistoru(inMemoryTaskManager.getTaskForId(Integer.parseInt(data[i])));
                            } else if (inMemoryTaskManager.epicContainsKey(Integer.parseInt(data[i]))) {
                                addHistoru(inMemoryTaskManager.getEpicForId(Integer.parseInt(data[i])));
                            } else if (inMemoryTaskManager.subtaskContainsKey(Integer.parseInt(data[i]))) {
                                addHistoru(inMemoryTaskManager.getSubtasksForId(Integer.parseInt(data[i])));
                            }
                        }
                    }
                }
            }
            inMemoryTaskManager.setIdCounter(maxIdNumber);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка: выход за пределы массива. Проверьте формат данных в файле.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка преобразования числа: " + e.getMessage());
        }

    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                writer.write(CSVTaskFormat.toString(task) + "\n");
                writer.newLine();
            }
            for (Epic epic : getEpics()) {
                writer.write(CSVTaskFormat.toString(epic) + "\n");
                writer.newLine();
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(CSVTaskFormat.toString(subtask) + "\n");
                writer.newLine();
            }
            writer.write("\n");
            writer.write(CSVTaskFormat.toString(inMemoryTaskManager.getHistory()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addTask(Task task) {
        if (task.getTaskType() == TaskType.TASK) {
            inMemoryTaskManager.addInMapTask(task);
        } else if (task.getTaskType() == TaskType.EPIC) {
            Epic epic = new Epic(task.getId(), task.getName(), task.getDescription());
            epic.setStatus(task.getStatus());
            inMemoryTaskManager.addInMapEpic(epic);
        } else {
            Subtask subtask = new Subtask(task.getId(), task.getName(), task.getDescription(), task.getEpicId());
            subtask.setStatus(task.getStatus());
            inMemoryTaskManager.addInMapSubtask(subtask);
        }
        inMemoryTaskManager.setIdCounter(maxIdNumber);
    }

    public static void addHistoru(Task task) {
        inMemoryTaskManager.historyManager.add(task);
    }

    @Override
    public void addInMapTask(Task task) {
        super.addInMapTask(task);
        save();
    }

    @Override
    public void addInMapEpic(Epic epic) {
        super.addInMapEpic(epic);
        save();
    }

    @Override
    public void addInMapSubtask(Subtask subtask) {
        super.addInMapSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskForId(int id) {
        Task task = super.getTaskForId(id);
        save();
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicForId(int id) {
        Epic epic = super.getEpicForId(id);
        save();
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtasksForId(int id) {
        Subtask subtask = super.getSubtasksForId(id);
        save();
        historyManager.add(subtask);
        return subtask;
    }


}
