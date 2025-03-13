package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public EpicHandler() {
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
                        Epic epic = taskManager.getEpicForId(id);
                        if (epic != null) {
                            String jsonResponse = gson.toJson(epic);
                            sendText(httpExchange, jsonResponse);
                        } else {
                            String errorMessage = "Задача Epic с таким номером id не найдена";
                            sendNotFound(httpExchange, errorMessage);
                        }
                    } else {
                        ArrayList<Epic> epics = taskManager.getEpics();
                        String jsonResponse = gson.toJson(epics);
                        sendText(httpExchange, jsonResponse);
                    }
                    break;
                case "DELETE":
                    if (uriParts.length == 3) {
                        int id = Integer.parseInt(uriParts[2]);
                        Epic epic = taskManager.getEpicForId(id);
                        if (epic != null) {
                            taskManager.removeEpicForId(id);
                            String message = "Задача Epic удалена";
                            sendText(httpExchange, message);
                        } else {
                            String errorMessage = "Задача Epic с таким номером id не найдена";
                            sendNotFound(httpExchange, errorMessage);
                        }
                    } else {
                        taskManager.removeAllEpics();
                        String message = "Все задачи Epic удалены";
                        sendText(httpExchange, message);
                    }
                    break;
                case "POST":
                    String[] epicInfo = uriParts[2].split(",");
                    String message;
                    if (epicInfo.length == 3) {
                        Epic epic = new Epic(Integer.parseInt(epicInfo[0]), epicInfo[1], epicInfo[2]);
                        taskManager.updateEpic(epic);
                        message = "Задача Epic обновлена";
                    } else {
                        Epic epic = new Epic(epicInfo[0], epicInfo[1]);
                        taskManager.addInMapEpic(epic);
                        message = "Задача Epic добавлена";
                    }
                    sendText(httpExchange, message);
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
