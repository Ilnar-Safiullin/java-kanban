package manager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("./resources/task.csv");
        FileBackedTaskManager managerRestored = new FileBackedTaskManager(file);
        System.out.println(managerRestored.getTasks());

        Epic epic1 = new Epic("Test 9", "Test 9");
        managerRestored.addInMapEpic(epic1);
        Subtask subtask1 = new Subtask("Test 9", "Test 9", epic1.getId(), Duration.ofMinutes(10), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Test 9", "Test 9", epic1.getId(), Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(15L));
        managerRestored.addInMapSubtask(subtask1);
        managerRestored.addInMapSubtask(subtask2);
        System.out.println(managerRestored.getTasks());
        System.out.println("===============");
        /*
        Сергей Привет, Спасибо большое за рекомендации. С тестами, я пытался сделать как у тебя в примере с protected T taskManager
        в тестовом Абстрактном классе, но не смог. И в чате у людей и у курратора спрашивал, но никто ничего не сказал. Не смог в итоге запустить тесты в абстрактном классе.
        В итоге собрал тесты в таком виде. Я пытался полю <protected T TaskManager> в методе setUp() сделать: taskManager = (T) new InMemoryTaskManager();
        Но тесты не запускались и писало ошибку nullpointexception. Он какбудто вообще не мог запустить менеджер. Ужасный абстрактный класс (
        Спасибо за проверку и хорошего тебе дня!
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
