import java.util.ArrayList;

class Epic extends Task {
    protected ArrayList<Integer> subTaskId;

    public Epic(String name, String description) {
        super(name, description);
        subTaskId = new ArrayList<>();
    }
}