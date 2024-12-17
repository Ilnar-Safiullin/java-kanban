import java.util.ArrayList;

public class GlobalTask {
    protected Status status;
    protected String description;
    protected ArrayList<SubTask> subTask = new ArrayList<>();
    private long id;
    private static long idCounter = 0;

    public GlobalTask(Status status, String description) {
        this.status = status;
        this.description = description;
        generateId();
    }

    public long generateId() {
        id = idCounter;
        idCounter++;
        return id;
    }

    public long getId() {
        return this.id;
    }



}
