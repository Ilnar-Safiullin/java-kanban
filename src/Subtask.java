class Subtask extends Task {
    private int EpicId;

    public int getEpicId() {
        return EpicId;
    }

    public void setEpicId(int epicId) {
        EpicId = epicId;
    }

    public Subtask(String description, Status status) {
        super(description, status);
    }
}
