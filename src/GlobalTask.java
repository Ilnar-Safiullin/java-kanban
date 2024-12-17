import java.util.ArrayList;

public class GlobalTask {
    protected Status status;
    protected String description;
    protected ArrayList<Task> subTask = new ArrayList<>();
    private long id;
    private static long idCounter = 3;

    public GlobalTask(Status status, String description) {
        this.status = status;
        this.description = description;
        generateId();
    }

    public long generateId() {
        id = idCounter * 3;
        idCounter++;
        return id;
    }

    public long getId() {
        return this.id;
    }



}
