package task;

public class Subtask extends Task {
    protected Integer epicId;

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        if (this.epicId == null) {
            this.epicId = epicId;
        }
    }
}
