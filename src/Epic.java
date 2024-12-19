class Epic extends Task {
    private int EpicId;

    protected static int idCounters = 0;

    public Epic(String description, Status status) {
        super(description, status);
        generateEpicId();
    }

    public int generateEpicId() {
        EpicId = idCounters;
        idCounters++;
        return EpicId;
    }

    public int getEpicId() {
        return this.EpicId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + getId() +
                ", EpicId=" + EpicId +
                '}';
    }
}