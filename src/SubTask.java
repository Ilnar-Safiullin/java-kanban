class SubTask {
    protected Status status;
    protected String description;
    protected int taskId;
    private static int idCounter = 0;

    public SubTask(Status status, String description) {
        this.status = status;
        this.description = description;
        generateId();
    }

    public int generateId() {
        taskId = idCounter;
        idCounter++;
        return taskId;
    }

}