package manager;

import task.*;


public class CSVTaskFormat {

    public static String toString(Task task) {
        if (TaskType.SUBTASK == task.getTaskType()) {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getName() + "," + subtask.getStatus() + ","
                    + subtask.getDescription() + "," + subtask.getEpicId();
        }
        return (task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription());
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final Integer id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];
        if (TaskType.SUBTASK == type) {
            final Integer epicId = Integer.parseInt(values[5]);
            Subtask subtask = new Subtask(id, name, description, epicId);
            subtask.setStatus(status);
            return subtask;
        }
        if (type == TaskType.TASK) {
            Task task = new Task(id, name, description);
            task.setStatus(status);
            return task;
        } else {
            Epic epic = new Epic(id, name, description);
            epic.setStatus(status);
            return epic;
        }
    }
}
