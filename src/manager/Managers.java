package manager;

import java.io.File;

public class Managers {

    public static FileBackedTaskManager getDefault() {
        return new FileBackedTaskManager(new File("C:\\Users\\Ilnar\\first-project\\resources\\task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
