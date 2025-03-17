package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String requestPath = httpExchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");

        switch (method) {
            case "GET":
                getEpic(httpExchange, pathParts);
                break;
            case "DELETE":
                deleteEpic(httpExchange, pathParts);
                break;
            case "POST":
                postEpic(httpExchange);
                break;
            default:
                sendText(httpExchange, "Метод не поддерживается", 404);
                break;
        }
    }

    public void getEpic(HttpExchange httpExchange, String[] pathParts) throws IOException {
        try {
            if (pathParts.length == 3 && pathParts[1].equals("epics")) {
                Epic epic = taskManager.getEpicForId(Integer.parseInt(pathParts[2]));
                String jsonResponse = gson.toJson(epic);
                if (epic != null) {
                    sendText(httpExchange, jsonResponse, 200);
                } else {
                    throw new NotFoundException("Задачи с таким номером id нет");
                }
            } else if (pathParts.length == 2 && pathParts[1].equals("epics")) {
                ArrayList<Epic> epics = taskManager.getEpics();
                String jsonResponse = gson.toJson(epics);
                sendText(httpExchange, jsonResponse, 200);
            } else if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) { //getSubTaskIdList() если использовать это то тогда мы должны буем получить сперва эпик (этот метод класса эпик же), что добавит его в историю, а ты сказал так нельзя
                List<Integer> subtasksId = taskManager.getSubtasksByEpic(Integer.parseInt(pathParts[2]));
                if (subtasksId != null) {
                    String jsonResponse = gson.toJson(subtasksId);
                    sendText(httpExchange, jsonResponse, 200);
                } else {
                    throw new NotFoundException("Задачи с таким номером id нет");
                }
            }
        } catch (NotFoundException notFoundExp) {
            sendText(httpExchange, notFoundExp.getMessage(), 404);
        } catch (Exception exp) {
            sendText(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void deleteEpic(HttpExchange httpExchange, String[] pathParts) throws IOException {
        try {
            if (pathParts.length == 3 && pathParts[1].equals("epics")) {
                taskManager.removeEpicForId(Integer.parseInt(pathParts[2]));
                sendText(httpExchange, "Задача удалена", 200);
            } else if (pathParts.length == 2 && pathParts[1].equals("epics")) {
                taskManager.removeAllEpics();
                String message = "Все задачи Epic удалены";
                sendText(httpExchange, message, 200);
            }
        } catch (Exception exp) {
            sendText(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }

    public void postEpic(HttpExchange httpExchange) throws IOException {
        try {
            InputStream requestBody = httpExchange.getRequestBody();
            String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            Epic epicDeserialized = gson.fromJson(requestBodyString, Epic.class);
            if (epicDeserialized == null) {
                sendText(httpExchange, "Не удалось преобразовать тело запроса в задачу!", 404);
            } else if (epicDeserialized.getId() != null) {
                taskManager.updateEpic(epicDeserialized);
                sendText(httpExchange, "Задача Epic обновлена", 201);
            } else {
                taskManager.addInMapEpic(epicDeserialized);
                sendText(httpExchange, "Задача Epic добавлена", 201);
            }
        } catch (Exception exp) {
            sendText(httpExchange, "При выполнении запроса возникла ошибка " + exp.getMessage(), 404);
        }
    }
}
