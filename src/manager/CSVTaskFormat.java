package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;


public class CSVTaskFormat {

    public static String toString(Task task) {
        if (TaskType.SUBTASK == task.getTaskType()) {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getName() + "," + subtask.getStatus() +
                    "," + subtask.getDescription() + "," + subtask.getDuration() + "," + subtask.getStartTime() + "," +
                    subtask.getEpicId();
        }
        return (task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.getDuration() + "," + task.getStartTime());
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final Integer id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];
        final Duration duration = Duration.parse(values[5]);
        final LocalDateTime startTime = LocalDateTime.parse(values[6]);
        if (TaskType.SUBTASK == type) {
            final Integer epicId = Integer.parseInt(values[7]);
            Subtask subtask = new Subtask(id, name, description, epicId, duration, startTime);
            subtask.setStatus(status);
            return subtask;
        }
        if (type == TaskType.TASK) {
            Task task = new Task(id, name, description, duration, startTime);
            task.setStatus(status);
            return task;
        } else {
            Epic epic = new Epic(id, name, description);
            epic.setStatus(status);
            epic.setDuration(duration);
            epic.setStartTime(startTime);
            return epic;
        }
    }
}
