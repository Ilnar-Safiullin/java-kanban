package server;

import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private HttpTaskServer server = new HttpTaskServer();
    private TaskManager manager = Managers.getDefault();
    private Task task;
    private Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    public void setUP() throws IOException {
        manager.removeAllTask();
        manager.removeAllEpics();
        manager.removeAllSubtasks();
        server.startServer();
        task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
        manager.addInMapTask(task);
    }

    @AfterEach
    public void close() throws IOException {
        server.stopServer();
    }

    @Test
    public void historyGET() throws IOException, InterruptedException {
        manager.getTaskForId(task.getId());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        List<Task> receivedTasks = gson.fromJson(responseBody, new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "HistoryHandler вернул не тот ответ");
        assertEquals(1, receivedTasks.size(), "HistoryHandler не вернул List<Tasks>");
    }
}
