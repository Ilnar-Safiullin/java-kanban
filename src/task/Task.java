package task;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Status status;
    protected Integer id;

    public Task(String name, String description) {
        this.description = description;
        this.status = Status.NEW;
        this.name = name;
    }

    public Task(Integer id, String name, String description) {
        this.id = id;
        this.description = description;
        this.status = Status.NEW;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Class.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id;
    }

    public void setId(int id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }
}
