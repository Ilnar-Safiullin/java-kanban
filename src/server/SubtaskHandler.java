package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String requestPath = httpExchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        switch (method) {
            case "GET":
                getSubtask(httpExchange, pathParts);
                break;
            case "DELETE":
                deleteSubtask(httpExchange, pathParts);
                break;
            case "POST":
                postSubtask(httpExchange);
                break;
            default:
                sendText(httpExchange, "Метод не поддерживается", 404);
                break;
        }
    }

    public void getSubtask(HttpExchange httpExchange, String[] pathParts) throws IOException {
        try {
            if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                Subtask subtask = taskManager.getSubtasksForId(Integer.parseInt(pathParts[2]));
                String jsonResponse = gson.toJson(subtask);
                if (subtask != null) {
                    sendText(httpExchange, jsonResponse, 200);
                } else {
                    throw new NotFoundException("Задачи с таким номером id нет");
                }
            } else if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                ArrayList<Subtask> subtasks = taskManager.getSubtasks();
                String jsonResponse = gson.toJson(subtasks);
                sendText(httpExchange, jsonResponse, 200);
            }
        } catch (NotFoundException notFoundExp) {
            sendText(httpExchange, notFoundExp.getMessage(), 404);
        } catch (Exception exp) {
            sendText(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void deleteSubtask(HttpExchange httpExchange, String[] pathParts) throws IOException {
        try {
            if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                taskManager.removeSubtaskForId(Integer.parseInt(pathParts[2]));
                sendText(httpExchange, "Задача удалена", 200);
            } else if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                taskManager.removeAllSubtasks();
                String message = "Все задачи Subtask удалены";
                sendText(httpExchange, message, 200);
            }
        } catch (Exception exp) {
            sendText(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void postSubtask(HttpExchange httpExchange) throws IOException {
        try {
            InputStream requestBody = httpExchange.getRequestBody();
            String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtaskDeserialized = gson.fromJson(requestBodyString, Subtask.class);
            if (subtaskDeserialized == null) {
                sendText(httpExchange, "Не удалось преобразовать тело запроса в задачу!", 404);
            } else if (subtaskDeserialized.getId() != null) {
                taskManager.updateSubtask(subtaskDeserialized);
                sendText(httpExchange, "Задача Subtask обновлена", 201);
            } else {
                taskManager.addInMapSubtask(subtaskDeserialized);
                sendText(httpExchange, "Задача Subtask добавлена", 201);
            }
        } catch (Exception exp) {
            sendText(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }
}