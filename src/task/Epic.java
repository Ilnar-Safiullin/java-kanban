package task;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTaskIdList;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subTaskIdList = new ArrayList<>();
    }

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
        subTaskIdList = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Class.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}