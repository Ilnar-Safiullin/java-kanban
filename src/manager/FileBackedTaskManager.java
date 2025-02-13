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

    public static void main(String[] args) {
        File file = new File("./resources/task.csv");
        FileBackedTaskManager managerRestored = FileBackedTaskManager.loadFromFile(file);
        System.out.println(managerRestored.getTasks());
        System.out.println(managerRestored.getEpics());
        System.out.println(managerRestored.getSubtasks());
        System.out.println("===============");
        Task task8 = new Task("Test 9", "Test 9");
        managerRestored.addInMapTask(task8);
        System.out.println(managerRestored.getTasks());
        System.out.println("===============");
        managerRestored.removeTaskForId(1);
        System.out.println(managerRestored.getTasks());
        /*
        Спасибо большое за твои рекомендации очень и очень помогло, вроде все сделал) Хорошего тебе дня!
         */


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
                        break;
                    case SUBTASK:
                        managerRestored.subtasks.put(task.getId(), (Subtask) task);
                        break;
                    case EPIC:
                        managerRestored.epics.put(task.getId(), (Epic) task);
                        break;
                }
            }
            if (!managerRestored.subtasks.isEmpty()) {
                for (Subtask subtask : managerRestored.subtasks.values()) {
                    Epic epic = managerRestored.epics.get(subtask.getEpicId());
                    epic.getSubTaskIdList().add(subtask.getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла");
        }
        return managerRestored;
    }

    protected void save() {
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
