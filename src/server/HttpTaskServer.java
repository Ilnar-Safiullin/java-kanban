package server;

import com.sun.net.httpserver.HttpServer;


import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;


    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.startServer();

        /*
        Сергей Привет. Спасибо большое за ответ. Я там пытался отменить отправку чтобы не дергать тебя. В итоге у меня
        еще была проблема в том, что если у Таски к примеру поле StartTime == null, то ее не получается перевести в Json.
        В итоге решил просто в <POST> добавить другой конструктор (который со всеми полями)
        upd. Мне умные люди из чата сказали как обойдти ошибку с null в startTime в LocalDateTimeAdapter. В итоге все работает вроде
         */
    }

    public void startServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/tasks", new TaskHandler()); // связываем путь и обработчик
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }


}
