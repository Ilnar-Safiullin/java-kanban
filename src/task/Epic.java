package task;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTaskIdList;

    public Epic(String name, String description) {
        super(name, description);
        subTaskIdList = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }
}