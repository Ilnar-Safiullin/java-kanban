package server;

import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrioritizedHandlerTest {
    private HttpTaskServer server = new HttpTaskServer();
    private TaskManager manager = Managers.getDefault();
    private Task task;
    private Epic epic;
    private Subtask subtask;
    private Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    public void setUP() throws IOException {
        manager.removeAllTask();
        manager.removeAllEpics();
        manager.removeAllSubtasks();
        server.startServer();
        task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
        epic = new Epic("Test", "Testing Epic");
        manager.addInMapEpic(epic);
        subtask = new Subtask("Subtask", "Testing subtask", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now().minusMinutes(15));
        manager.addInMapTask(task);
        manager.addInMapSubtask(subtask);
    }

    @AfterEach
    public void close() throws IOException {
        server.stopServer();
    }

    @Test
    public void prioritizedGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        List<Task> receivedTasks = gson.fromJson(responseBody, new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "PrioritizedHandler вернул не тот ответ");
        assertEquals(2, receivedTasks.size(), "PrioritizedHandler не вернул List<Tasks>");
    }
}
