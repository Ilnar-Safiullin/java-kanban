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
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public TaskHandler() {
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
                        Task task = taskManager.getTaskForId(id);
                        if (task != null) {
                            String jsonResponse = gson.toJson(task);
                            sendText(httpExchange, jsonResponse);
                        } else {
                            String errorMessage = "Задача Task с таким номером id не найдена";
                            sendNotFound(httpExchange, errorMessage);
                        }
                    } else {
                        ArrayList<Task> tasks = taskManager.getTasks();
                        String jsonResponse = gson.toJson(tasks);
                        sendText(httpExchange, jsonResponse);
                    }
                    break;
                case "DELETE":
                    if (uriParts.length == 3) {
                        int id = Integer.parseInt(uriParts[2]);
                        Task task = taskManager.getTaskForId(id);
                        if (task != null) {
                            taskManager.removeTaskForId(id);
                            String message = "Задача Task удалена";
                            sendText(httpExchange, message);
                        } else {
                            String errorMessage = "Задача Task с таким номером id не найдена";
                            sendNotFound(httpExchange, errorMessage);
                        }
                    } else {
                        taskManager.removeAllTask();
                        String message = "Все задачи Task удалены";
                        sendText(httpExchange, message);
                    }
                    break;
                case "POST":
                    String[] taskInfo = uriParts[2].split(",");
                    String message;
                    if (taskInfo.length == 5) {
                        Task task = new Task(Integer.parseInt(taskInfo[0]), taskInfo[1], taskInfo[2],
                                Duration.parse(taskInfo[3]), LocalDateTime.parse(taskInfo[4]));
                        taskManager.updateTask(task);
                        message = "Задача Task обновлена";
                    } else {
                        Task task = new Task(taskInfo[0], taskInfo[1]);
                        taskManager.addInMapTask(task);
                        message = "Задача Task добавлена";
                        System.out.println("Dobavleno");
                        System.out.println(taskManager.getTasks());
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
