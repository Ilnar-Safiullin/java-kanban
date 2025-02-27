package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected Integer epicId;

    public Integer getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(id, name, description, duration, startTime);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        if (this.epicId == null) {
            this.epicId = epicId;
        }
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Class.Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", epicId=" + epicId +
                '}';
    }

}
