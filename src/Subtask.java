class Subtask extends Task {
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

}
