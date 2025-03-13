package manager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager managerRestored = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { // Пропускаем первую строку
                    firstLine = false;
                    continue;
                }
                Task task = CSVTaskFormat.taskFromString(line);
                if (task.getId() > managerRestored.idCounter) {
                    managerRestored.idCounter = task.getId() + 1;
                }
                switch (task.getTaskType()) {
                    case TASK:
                        managerRestored.tasks.put(task.getId(), task);
                        managerRestored.prioritizedTasks.add(task);
                        break;
                    case SUBTASK:
                        managerRestored.subtasks.put(task.getId(), (Subtask) task);
                        managerRestored.prioritizedTasks.add(task);
                        break;
                    case EPIC:
                        managerRestored.epics.put(task.getId(), (Epic) task);
                        break;
                }
            }
            for (Subtask subtask : managerRestored.subtasks.values()) {
                Epic epic = managerRestored.epics.get(subtask.getEpicId());
                epic.getSubTaskIdList().add(subtask.getId());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла");
        }
        return managerRestored;
    }

    private void save() {
        Path path = file.toPath();
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                throw new ManagerSaveException("Не удалось создать файл");
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                writer.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(CSVTaskFormat.toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(CSVTaskFormat.toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить данные в файл");
        }
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
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeTaskForId(int id) {
        super.removeTaskForId(id);
        save();
    }

    @Override
    public void removeEpicForId(int id) {
        super.removeEpicForId(id);
        save();
    }

    @Override
    public void removeSubtaskForId(int idSub) {
        super.removeSubtaskForId(idSub);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}
