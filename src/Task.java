import java.util.Objects;

public class Task {
    protected String description;
    protected Status status;
    private int id;
    protected static int idCounter = 0;

    public Task(String description, Status status) {
        this.description = description;
        this.status = status;
        generateId();
    }

    public int generateId() {
        id = idCounter;
        idCounter++;
        return id;
    }

    public int getId() {
        return this.id;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return "Task{" + "description " +
                description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, status, id);
    }

}
