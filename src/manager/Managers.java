package manager;

import java.io.File;

public class Managers {

    public static InMemoryTaskManager getDefault() {
        return new FileBackedTaskManager(new File(".\\resources\\task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
