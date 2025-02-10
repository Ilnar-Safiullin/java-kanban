package manager;

import task.Task;

import java.util.ArrayList;
import java.util.Map;

public interface HistoryManager {

    void add(Task task);

    ArrayList<Task> getHistory();

    void remove(int id);

}

