package server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import manager.InMemoryTaskManager;
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


public class TaskHandlerTest {
    private TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer server;
    private Task task;
    private Gson gson = HttpTaskServer.getGson();
    {
        try {
            server = new HttpTaskServer(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUP() throws IOException {
        manager.removeAllTask();
        manager.removeAllEpics();
        manager.removeAllSubtasks();
        server.startServer();
        task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
    }

    @AfterEach
    public void close() throws IOException {
        server.stopServer();
    }

    @Test
    public void testAddTaskPOST() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient(); // создаём HTTP-клиент и запрос
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());// вызываем рест, отвечающий за создание задач
        assertEquals(201, response.statusCode(), "TaskHandler вернул не тот ответ при добавлении Таски"); // проверяем код ответа

        assertEquals(1, manager.getTasks().size(), "TaskHandler не добавил Таску");
        assertEquals(task.getName(), manager.getTasks().getFirst().getName(), "Неправильная десериализация  Таски");
    }

    @Test
    public void testUpdateTaskPOST() throws IOException, InterruptedException {
        manager.addInMapTask(task);
        Task task2 = new Task(task.getId(), "Test New", "Testing task 2", Duration.ofMinutes(5),
                LocalDateTime.now().minusMinutes(15L));
        String taskJson = gson.toJson(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "TaskHandler вернул не тот ответ при обновлении Таски");
        assertEquals(task.getName(), task2.getName(), "TaskHandler не обновил Таску");
    }

    @Test
    public void testTaskGET() throws IOException, InterruptedException {
        manager.addInMapTask(task);
        String taskId = gson.toJson(task.getId());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks?taskId=" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body(); // получаем JSON-строку из ответа
        List<Task> receivedTasks = gson.fromJson(responseBody, new TypeToken<List<Task>>() {
        }.getType());// десериализуем JSON-строку в объект Task

        assertEquals(200, response.statusCode(), "TaskHandler вернул не тот ответ при запросе Таски");
        assertEquals(task, receivedTasks.getFirst(), "TaskHandler вернул не ту Таску");
    }

    @Test
    public void testTaskGetAllTasksGET() throws IOException, InterruptedException {
        manager.addInMapTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        List<Task> receivedTasks = gson.fromJson(responseBody, new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(200, response.statusCode(), "TaskHandler вернул не тот ответ при запросе всех Тасок");
        assertEquals(1, receivedTasks.size(), "TaskHandler не вернул Таски");
    }

    @Test
    public void testTaskDelete() throws IOException, InterruptedException {
        manager.addInMapTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "TaskHandler вернул не тот ответ при удалении Таски");
        assertEquals(0, manager.getTasks().size(), "TaskHandler не удалил Таску");
    }
}
