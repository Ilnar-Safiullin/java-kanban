package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public TaskHandler() {
        this.taskManager = Managers.getDefault();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String requestPath = httpExchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        switch (method) {
            case "GET":
                getTask(httpExchange, pathParts);
                break;
            case "DELETE":
                deleteTask(httpExchange, pathParts);
                break;
            case "POST":
                postTask(httpExchange);
                break;
            default:
                sendNotFound(httpExchange, "Метод не поддерживается", 404);
                break;
        }
    }

    public void getTask(HttpExchange httpExchange, String[] pathParts) throws IOException {
        try {
            if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
                Task task = taskManager.getTaskForId(Integer.parseInt(pathParts[2]));
                String jsonResponse = gson.toJson(task);
                if (task != null) {
                    sendText(httpExchange, jsonResponse, 200);
                } else {
                    throw new NotFoundException("Задачи с таким номером id нет");
                }
            } else if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                ArrayList<Task> tasks = taskManager.getTasks();
                String jsonResponse = gson.toJson(tasks);
                sendText(httpExchange, jsonResponse, 200);
            }
        } catch (NotFoundException notFoundExp) {
            sendNotFound(httpExchange, notFoundExp.getMessage(), 404);
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void deleteTask(HttpExchange httpExchange, String[] pathParts) throws IOException {
        try {
            if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
                Task task = taskManager.getTaskForId(Integer.parseInt(pathParts[2]));
                if (task != null) {
                    taskManager.removeTaskForId(Integer.parseInt(pathParts[2]));
                    sendText(httpExchange, "Задача Task удалена", 200);
                } else {
                    throw new NotFoundException("Задачи с таким номером id нет");
                }
            } else if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                taskManager.removeAllTask();
                String message = "Все задачи Task удалены";
                sendText(httpExchange, message, 200);
            }
        } catch (NotFoundException notFoundExp) {
            sendNotFound(httpExchange, notFoundExp.getMessage(), 404);
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void postTask(HttpExchange httpExchange) throws IOException {
        try {
            InputStream requestBody = httpExchange.getRequestBody();
            String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            Task taskDeserialized = gson.fromJson(requestBodyString, Task.class);
            if (taskDeserialized == null) {
                sendNotFound(httpExchange, "Не удалось преобразовать тело запроса в задачу!", 404);
            } else if (taskDeserialized.getId() != null) {
                taskManager.updateTask(taskDeserialized);
                sendText(httpExchange, "Задача Task обновлена", 201);
            } else {
                taskManager.addInMapTask(taskDeserialized);
                sendText(httpExchange, "Задача Task добавлена", 201);
            }
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }
}
