package manager;

import java.io.File;

public class Managers {
    private static TaskManager taskManager;


    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new FileBackedTaskManager(new File(".\\resources\\task.csv"));
        }
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInMemoryTaskManger() {
        if (taskManager == null) {
            taskManager = new InMemoryTaskManager();
        }
        return taskManager;
    }
}
