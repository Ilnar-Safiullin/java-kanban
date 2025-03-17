package server;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
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

public class HistoryHandlerTest {
    private TaskManager manager;
    private HttpTaskServer server;
    private Task task;
    private Epic epic;
    private Gson gson = HttpTaskServer.getGson();


    @BeforeEach
    public void setUP() throws IOException {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.startServer();
        task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
        epic = new Epic("Test", "Testing Epic");
        manager.addInMapTask(task);
        manager.addInMapEpic(epic);
    }

    @AfterEach
    public void close() throws IOException {
        server.stopServer();
    }

    @Test
    public void historyGET() throws IOException, InterruptedException {
        manager.getEpicForId(epic.getId());
        manager.getTaskForId(task.getId());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        List<Task> receivedTasks = gson.fromJson(responseBody, new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "HistoryHandler вернул не тот ответ");
        assertEquals(2, receivedTasks.size(), "HistoryHandler не вернул историю List<Tasks>");
    }
}
