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


public class EpicHandlerTest {
    private TaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer server;
    private Epic epic;
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
    }

    @AfterEach
    public void close() throws IOException {
        server.stopServer();
    }

    @Test
    public void testAddEpicPOST() throws IOException, InterruptedException {
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient(); // создаём HTTP-клиент и запрос
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());// вызываем рест, отвечающий за создание задач
        assertEquals(201, response.statusCode(), "EpicHandler вернул не тот ответ при добавлении Epic"); // проверяем код ответа

        assertEquals(1, manager.getEpics().size(), "EpicHandler не добавил Subtask");
        assertEquals(epic.getName(), manager.getEpics().getFirst().getName(), "Неправильная десериализация Epic");
    }

    @Test
    public void testUpdateEpicPOST() throws IOException, InterruptedException {
        manager.addInMapEpic(epic);
        Epic epic2 = new Epic(epic.getId(), "Test New", "Testing epic 2");
        String epicJson = gson.toJson(epic2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "EpicHandler вернул не тот ответ при обновлении Epica");
        assertEquals(epic.getName(), epic2.getName(), "EpicHandler не обновил Epic");
    }

    @Test
    public void testEpicGET() throws IOException, InterruptedException {
        manager.addInMapEpic(epic);
        String epicId = gson.toJson(epic.getId());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics?epicId=" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body(); // получаем JSON-строку из ответа
        List<Epic> receivedEpics = gson.fromJson(responseBody, new TypeToken<List<Epic>>() {
        }.getType());// десериализуем JSON-строку в объект Epic

        assertEquals(200, response.statusCode(), "EpicHandler вернул не тот ответ при запросе Epic");
        assertEquals(epic, receivedEpics.getFirst(), "EpicHandler вернул не тот Epic");
    }

    @Test
    public void testGetAllEpicsGET() throws IOException, InterruptedException {
        manager.addInMapEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        List<Epic> receivedEpics = gson.fromJson(responseBody, new TypeToken<List<Epic>>() {
        }.getType());

        assertEquals(200, response.statusCode(), "EpicHandler вернул не тот ответ при запросе всех Epicov");
        assertEquals(1, receivedEpics.size(), "EpicHandler не вернул Epici");
    }

    @Test
    public void testEpicDelete() throws IOException, InterruptedException {
        manager.addInMapEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "EpicHandler вернул не тот ответ при удалении Epic");
        assertEquals(0, manager.getEpics().size(), "EpicHandler не удалил Epic");
    }

    @Test
    public void testGetEpicsSubtask() throws IOException, InterruptedException, NotFoundException {
        manager.addInMapEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Testing subtask", epic.getId(), Duration.ofMinutes(5),
                LocalDateTime.now());
        manager.addInMapSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        List<Integer> receivedEpics = gson.fromJson(responseBody, new TypeToken<List<Integer>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertEquals(1, receivedEpics.size(), "EpicHandler не вернул SubtaskIdList у Epic");

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/epics/" + Integer.MAX_VALUE + "/subtasks/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(),
                "EpicHandler вернул не тот ответ если мы отправили не сущ-ий номер айди");
    }
}
