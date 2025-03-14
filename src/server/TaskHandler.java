package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public TaskHandler() {
        this.taskManager = Managers.getDefault();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        InputStream requestBody = httpExchange.getRequestBody();
        String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "GET":
                getTask(httpExchange, requestBodyString);
                break;
            case "DELETE":
                deleteTask(httpExchange, requestBodyString);
                break;
            case "POST":
                postTask(httpExchange, requestBodyString);
                break;
            default:
                sendNotFound(httpExchange, "Метод не поддерживается", 404);
                break;
        }
    }

    public void getTask(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            if (!requestBodyString.isEmpty()) {
                int id = Integer.parseInt(requestBodyString);
                Task task = taskManager.getTaskForId(id);
                String jsonResponse = gson.toJson(task);
                if (task != null) {
                    sendText(httpExchange, jsonResponse, 200);
                } else {
                    sendNotFound(httpExchange, "Задачи с таким номером id нет", 404);
                }
            } else {
                ArrayList<Task> tasks = taskManager.getTasks();
                String jsonResponse = gson.toJson(tasks);
                sendText(httpExchange, jsonResponse, 200);
            }
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void deleteTask(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            if (!requestBodyString.isEmpty()) {
                int id = Integer.parseInt(requestBodyString);
                Task task = taskManager.getTaskForId(id);
                if (task != null) {
                    taskManager.removeTaskForId(id);
                    sendText(httpExchange, "Задача Task удалена", 200);
                } else {
                    sendNotFound(httpExchange, "Задача Task с таким номером id не найдена", 404);
                }
            } else {
                taskManager.removeAllTask();
                String message = "Все задачи Task удалены";
                sendText(httpExchange, message, 200);
            }
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void postTask(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            String[] taskData = requestBodyString.split(",");
            String message;
            if (taskData.length == 5) {
                Task task = new Task(Integer.parseInt(taskData[0]), taskData[1], taskData[2],
                                    Duration.parse(taskData[3]), LocalDateTime.parse(taskData[4]));
                taskManager.updateTask(task);
                message = "Задача Task обновлена";
            } else {
                Task task = new Task(taskData[0], taskData[1]);
                taskManager.addInMapTask(task);
                message = "Задача Task добавлена";
            }
            sendText(httpExchange, message, 201);
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }
}
