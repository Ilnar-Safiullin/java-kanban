package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
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
        String requestURI = httpExchange.getRequestURI().toString();
        String[] uriParts = requestURI.split("/");

        try {
            switch (method) {
                case "GET":
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                            .create();
                    if (uriParts.length == 3) {
                        int id = Integer.parseInt(uriParts[2]);
                        Subtask subtask = taskManager.getSubtasksForId(id);
                        if (subtask != null) {
                            String jsonResponse = gson.toJson(subtask);
                            sendText(httpExchange, jsonResponse);
                        } else {
                            String errorMessage = "Задача Subtask с таким номером id не найдена";
                            sendNotFound(httpExchange, errorMessage);
                        }
                    } else {
                        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
                        String jsonResponse = gson.toJson(subtasks);
                        sendText(httpExchange, jsonResponse);
                    }
                    break;
                case "DELETE":
                    if (uriParts.length == 3) {
                        int id = Integer.parseInt(uriParts[2]);
                        Subtask subtask = taskManager.getSubtasksForId(id);
                        if (subtask != null) {
                            taskManager.removeSubtaskForId(id);
                            String message = "Задача Subtask удалена";
                            sendText(httpExchange, message);
                        } else {
                            String errorMessage = "Задача Subtask с таким номером id не найдена";
                            sendNotFound(httpExchange, errorMessage);
                        }
                    } else {
                        taskManager.removeAllSubtasks();
                        String message = "Все задачи Subtask удалены";
                        sendText(httpExchange, message);
                    }
                    break;
                case "POST":
                    String[] subtaskInfo = uriParts[2].split(",");
                    String message;
                    if (subtaskInfo.length == 6) {
                        Subtask subtask = new Subtask(Integer.parseInt(subtaskInfo[0]), subtaskInfo[1], subtaskInfo[2],
                                Integer.parseInt(subtaskInfo[3]), Duration.parse(subtaskInfo[4]), LocalDateTime.parse(subtaskInfo[5]));
                        taskManager.updateSubtask(subtask);
                        message = "Задача Subtask обновлена";
                    } else {
                        Subtask subtask = new Subtask(subtaskInfo[0], subtaskInfo[1], Integer.parseInt(subtaskInfo[2]),
                                Duration.parse(subtaskInfo[3]), LocalDateTime.parse(subtaskInfo[4]));
                        taskManager.addInMapSubtask(subtask);
                        message = "Задача Subtask добавлена";
                    }
                    sendText(httpExchange, message);
                    System.out.println(message);
                    System.out.println(taskManager.getTasks());
                    break;
                default:
                    sendNotFound(httpExchange, "Метод не поддерживается");
                    break;
            }
        } catch (NumberFormatException e) {
            sendNotFound(httpExchange, "Неверный формат ID");
        } catch (Exception e) {
            sendNotFound(httpExchange, "Произошла ошибка: " + e.getMessage());
        }
    }

}