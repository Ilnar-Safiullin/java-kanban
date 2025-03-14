package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public EpicHandler() {
        this.taskManager = Managers.getDefault();
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        InputStream requestBody = httpExchange.getRequestBody();
        String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "GET":
                getEpic(httpExchange, requestBodyString);
                break;
            case "DELETE":
                deleteEpic(httpExchange, requestBodyString);
                break;
            case "POST":
                postEpic(httpExchange, requestBodyString);
                break;
            default:
                sendNotFound(httpExchange, "Метод не поддерживается", 404);
                break;
        }
    }

    public void getEpic(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            if (!requestBodyString.isEmpty()) {
                int id = Integer.parseInt(requestBodyString);
                Epic epic = taskManager.getEpicForId(id);
                String jsonResponse = gson.toJson(epic);
                if (epic != null) {
                    sendText(httpExchange, jsonResponse, 200);
                } else {
                    sendNotFound(httpExchange, "Задачи с таким номером id нет", 404);
                }
            } else {
                ArrayList<Epic> epics = taskManager.getEpics();
                String jsonResponse = gson.toJson(epics);
                sendText(httpExchange, jsonResponse, 200);
            }
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void deleteEpic(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            if (!requestBodyString.isEmpty()) {
                int id = Integer.parseInt(requestBodyString);
                Epic epic = taskManager.getEpicForId(id);
                if (epic != null) {
                    taskManager.removeEpicForId(id);
                    sendText(httpExchange, "Задача удалена", 200);
                } else {
                    sendNotFound(httpExchange, "Задача с таким номером id не найдена", 404);
                }
            } else {
                taskManager.removeAllEpics();
                String message = "Все задачи Epic удалены";
                sendText(httpExchange, message, 200);
            }
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void postEpic(HttpExchange httpExchange, String requestBodyString) throws IOException {
        try {
            String[] epicInfo = requestBodyString.split(",");
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
            sendText(httpExchange, message, 201);
        } catch (Exception exp) {
            sendNotFound(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }
}
