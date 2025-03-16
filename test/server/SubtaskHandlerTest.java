package server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SubtaskHandlerTest {
    private TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer server;
    private Epic epic;
    private Subtask subtask;
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
        epic = new Epic("Test", "Testing Epic");
        manager.addInMapEpic(epic);
        subtask = new Subtask("Subtask", "Testing subtask", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now());
    }

    @AfterEach
    public void close() throws IOException {
        server.stopServer();
    }

    @Test
    public void testAddSubtaskPOST() throws IOException, InterruptedException {
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient(); // создаём HTTP-клиент и запрос
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());// вызываем рест, отвечающий за создание задач
        assertEquals(201, response.statusCode(), "SubtaskHandler вернул не тот ответ при добавлении Subtask"); // проверяем код ответа

        assertEquals(1, manager.getSubtasks().size(), "SubtaskHandler не добавил Subtask");
        assertEquals(subtask.getName(), manager.getSubtasks().getFirst().getName(), "Неправильная десериализация Subtask");
    }

    @Test
    public void testUpdateSubtaskPOST() throws IOException, InterruptedException {
        manager.addInMapSubtask(subtask);
        Subtask subtask2 = new Subtask(subtask.getId(), "Test New", "Testing subtask 2", epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now().minusMinutes(15L));
        String subtaskJson = gson.toJson(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "SubtaskHandler вернул не тот ответ при обновлении Subtask");
        assertEquals(subtask.getName(), subtask2.getName(), "SubtaskHandler не обновил Subtask");
    }

    @Test
    public void testSubtaskGET() throws IOException, InterruptedException {
        manager.addInMapSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks?subtaskId=" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body(); // получаем JSON-строку из ответа
        List<Subtask> receivedSubtasks = gson.fromJson(responseBody, new TypeToken<List<Subtask>>() {
        }.getType()); // десериализуем JSON-строку в объект Epic

        assertEquals(200, response.statusCode(), "SubtaskHandler вернул не тот ответ при запросе Subtask");
        assertEquals(subtask, receivedSubtasks.getFirst(), "SubtaskHandler вернул не тот Subtask");
    }

    @Test
    public void testGetAllSubtasksGET() throws IOException, InterruptedException {
        manager.addInMapSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        List<Subtask> receivedSubtasks = gson.fromJson(responseBody, new TypeToken<List<Subtask>>() {
        }.getType());

        assertEquals(200, response.statusCode(), "SubtaskHandler вернул не тот ответ при запросе всех Subtask");
        assertEquals(1, receivedSubtasks.size(), "SubtaskHandler не вернул Subtask");
    }

    @Test
    public void testSubtaskDelete() throws IOException, InterruptedException {
        manager.addInMapSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "SubtaskHandler вернул не тот ответ при удалении Subtask");
        assertEquals(0, manager.getSubtasks().size(), "SubtaskHandler не удалил Subtask");
    }
}
