package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    static File file;
    private static Integer maxIdNumber = 0;
    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Ilnar\\Documents\\resources\\task.csv");
        FileBackedTaskManager managerRestored = FileBackedTaskManager.loadFromFile(file);
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println("История :" + inMemoryTaskManager.getHistory());
        Task task3 = new Task("Test 8", "Test 9");
        managerRestored.addInMapTask(task3);
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println("История :" + inMemoryTaskManager.getHistory());
        /*
        Сергей привет, я пока отправляю на проверку в контексте правильно ли я вообще двигаюсь? у меня inMemoryTaskManager
        находится в поле текущего класса, а это наверное не верный вариант. Я спрашивал у Куратора но он послал к ревью (

        Извини что дергаю тебя, боюсь просто что нужно будет все снести и заного делать, а я это то 3 дня рожаю уже. Я так
        думаю что мне нужно будет вообще все снести и по новой делать, хоть я и тестил здесь в мейне что он восстанавливается с файла,
        но должно ли это так выглядить хз. Спасибо большое!
         */
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager managerRestored = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 1) {
                    if (data[1].equals(TaskType.TASK.toString())) {
                        Task task = CSVTaskFormat.taskFromString(line);
                        inMemoryTaskManager.setIdCounter(task.getId());
                        addTask(task);
                        if (Integer.parseInt(data[0]) > maxIdNumber) {
                            maxIdNumber = Integer.parseInt(data[0]);
                        }
                    } else if (data[1].equals(TaskType.EPIC.toString())) {
                        Epic epic = (Epic) CSVTaskFormat.taskFromString(line);
                        inMemoryTaskManager.setIdCounter(epic.getId());
                        addTask(epic);
                        if (Integer.parseInt(data[0]) > maxIdNumber) {
                            maxIdNumber = Integer.parseInt(data[0]);
                        }
                    } else if (data[1].equals(TaskType.SUBTASK.toString())) {
                        Subtask subtask = (Subtask) CSVTaskFormat.taskFromString(line);
                        inMemoryTaskManager.setIdCounter(subtask.getId());
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
            ++maxIdNumber;
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
        File file = new File("C:\\Users\\Ilnar\\Documents\\resources\\task.csv");
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
            for (Task task : inMemoryTaskManager.getTasks()) {
                writer.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Epic epic : inMemoryTaskManager.getEpics()) {
                writer.write(CSVTaskFormat.toString(epic) + "\n");
            }
            for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
                writer.write(CSVTaskFormat.toString(subtask) + "\n");
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
        if (task.getTaskType() == TaskType.TASK) {
            inMemoryTaskManager.setIdCounter(maxIdNumber);
            ++maxIdNumber;
            inMemoryTaskManager.addInMapTask(task);
        } else if (task.getTaskType() == TaskType.EPIC) {
            Epic epic = new Epic(task.getId(), task.getName(), task.getDescription());
            inMemoryTaskManager.setIdCounter(maxIdNumber);
            ++maxIdNumber;
            inMemoryTaskManager.addInMapEpic(epic);
        } else {
            Subtask subtask = new Subtask(task.getId(), task.getName(), task.getDescription(), task.getEpicId());
            inMemoryTaskManager.setIdCounter(maxIdNumber);
            ++maxIdNumber;
            inMemoryTaskManager.addInMapSubtask(subtask);
        }
        save();
    }

    @Override
    public Task getTaskForId(int id) {
        Task task = super.getTaskForId(id);
        historyManager.add(task);
        save();
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
