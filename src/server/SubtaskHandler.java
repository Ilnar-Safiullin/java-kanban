package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public SubtaskHandler() {
        this.taskManager = Managers.getDefault();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        InputStream requestBody = httpExchange.getRequestBody();
        String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "GET":
                getSubtask(httpExchange, requestBodyString);
                break;
            case "DELETE":
                deleteSubtask(httpExchange, requestBodyString);
                break;
            case "POST":
                postSubtask(httpExchange, requestBodyString);
                break;
            default:
                sendNotFound(httpExchange, "Метод не поддерживается", 404);
                break;
        }
    }

    public void getSubtask(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            if (!requestBodyString.isEmpty()) {
                int id = Integer.parseInt(requestBodyString);
                Subtask subtask = taskManager.getSubtasksForId(id);
                String jsonResponse = gson.toJson(subtask);
                if (subtask != null) {
                    sendText(httpExchange, jsonResponse, 200);
                } else {
                    sendNotFound(httpExchange, "Задачи с таким номером id нет", 404);
                }
            } else {
                ArrayList<Subtask> subtasks = taskManager.getSubtasks();
                String jsonResponse = gson.toJson(subtasks);
                sendText(httpExchange, jsonResponse, 200);
            }
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void deleteSubtask(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            if (!requestBodyString.isEmpty()) {
                int id = Integer.parseInt(requestBodyString);
                Subtask subtask = taskManager.getSubtasksForId(id);
                if (subtask != null) {
                    taskManager.removeEpicForId(id);
                    sendText(httpExchange, "Задача удалена", 200);
                } else {
                    sendNotFound(httpExchange, "Задача с таким номером id не найдена", 404);
                }
            } else {
                taskManager.removeAllSubtasks();
                String message = "Все задачи Subtask удалены";
                sendText(httpExchange, message, 200);
            }
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void postSubtask(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            String[] subtaskInfo = requestBodyString.split(",");
            String message;
            if (subtaskInfo.length == 6) {
                Subtask subtask = new Subtask(Integer.parseInt(subtaskInfo[0]), subtaskInfo[1], subtaskInfo[2],
                                              Integer.parseInt(subtaskInfo[3]), Duration.parse(subtaskInfo[4]),
                                              LocalDateTime.parse(subtaskInfo[5]));
                taskManager.updateSubtask(subtask);
                message = "Задача Subtask обновлена";
            } else {
                Subtask subtask = new Subtask(subtaskInfo[0], subtaskInfo[1], Integer.parseInt(subtaskInfo[2]),
                                              Duration.parse(subtaskInfo[3]), LocalDateTime.parse(subtaskInfo[4]));
                taskManager.addInMapSubtask(subtask);
                message = "Задача Subtask добавлена";
            }
            sendText(httpExchange, message, 201);
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }
}