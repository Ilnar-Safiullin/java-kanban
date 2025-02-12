package manager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    {
        file = new File("C:\\Users\\Ilnar\\first-project\\resources\\task.csv");
    }

    private static Integer idCounter = 0;
    private static HashMap<Integer, Task> tasks = new HashMap<>(); // Пришлось их сделать статик иначе я просто не могу в методе loadFromFile добавить в мапу
    private static HashMap<Integer, Epic> epics = new HashMap<>();
    private static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static HistoryManager historyManager = Managers.getDefaultHistory();

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Ilnar\\first-project\\resources\\task.csv");
        FileBackedTaskManager managerRestored = FileBackedTaskManager.loadFromFile(file);
        System.out.println(managerRestored.getTasks());
        System.out.println(managerRestored.getEpics());
        System.out.println(managerRestored.getSubtasks());
        System.out.println("История :" + managerRestored.getHistory());

        /*
        Task task3 = new Task("Test 8", "Test 9");


        Пока пустое значение
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
                if (task.getId() > idCounter) {
                    idCounter = task.getId() + 1;
                }
                switch (task.getTaskType()) {
                    case TASK:
                        tasks.put(task.getId(), task);
                        break;
                    case SUBTASK:
                        subtasks.put(task.getId(), (Subtask) task);
                        break;
                    case EPIC:
                        epics.put(task.getId(), (Epic) task);
                        break;
                }
            }
            if (!subtasks.isEmpty()) {
                for (Subtask subtask : subtasks.values()) {
                    Epic epic = epics.get(subtask.getEpicId());
                    epic.getSubTaskIdList().add(subtask.getId());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка: выход за пределы массива. Проверьте формат данных в файле.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка преобразования числа: " + e.getMessage());
        }
        return managerRestored;
    }

    protected void save() {
        Path path = file.toPath();
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
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
            writer.write("\n");
            writer.write(CSVTaskFormat.toString(getHistory()));
        } catch (IOException e) {
            e.printStackTrace();
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
