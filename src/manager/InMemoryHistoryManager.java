package manager;

import task.Task;

import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> history = new ArrayList<>();
    private final int maxSize = 9;

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (history.size() > maxSize) {
            history.removeFirst();
        }
        history.add(task);
    }

}
