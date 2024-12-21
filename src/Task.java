import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Status status;
    protected int id;

    public Task(String name, String description) {
        this.description = description;
        this.status = Status.NEW;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }


    @Override
    public boolean equals(Object object) {
        Task task = (Task) object;
        return id == task.id && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, status, id);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
