package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public PrioritizedHandler() {
        this.taskManager = Managers.getInMemoryTaskManger();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                        .create();
                List<Task> tasks = taskManager.getPrioritizedTasks();
                if (tasks == null || tasks.isEmpty()) {
                    throw new NotFoundException("Задачи не найдены");
                }
                String jsonResponse = gson.toJson(tasks);
                sendText(httpExchange, jsonResponse);
            } catch (NotFoundException e) {
                sendNotFound(httpExchange, e.getMessage());
            } catch (Exception e) {
                sendNotFound(httpExchange, "Произошла ошибка: " + e.getMessage());
            }
        }
    }
}
