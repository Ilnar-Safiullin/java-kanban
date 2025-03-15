package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public HistoryHandler() {
        this.taskManager = Managers.getInMemoryTaskManger();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                ArrayList<Task> tasks = taskManager.getTasks();
                if (tasks == null || tasks.isEmpty()) {
                    throw new NotFoundException("Задачи не найдены");
                }
                String jsonResponse = gson.toJson(tasks);
                sendText(httpExchange, jsonResponse, 200);
            }
        } catch (NotFoundException notFoundExp) {
            sendNotFound(httpExchange, notFoundExp.getMessage(), 404);
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }
}
