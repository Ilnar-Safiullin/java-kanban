package task;

public class Subtask extends Task {
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
