package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.startServer();

        /*
        Сергей Привет. Спасибо большое за ответ. Я там пытался отменить отправку чтобы не дергать тебя. В итоге у меня
        еще была проблема в том, что если у Таски к примеру поле StartTime == null, то ее не получается перевести в Json.
        В итоге решил просто в <POST> добавить другой конструктор (который со всеми полями)
        upd. Мне умные люди из чата сказали как обойти ошибку с null в startTime в LocalDateTimeAdapter. В итоге все работает,
        вернул большой конструктор у таски

        Еще вроде как мы должны проверять на пересечение по времени и выдавать ошибку если есть пересечение. Я поспрашивал в группе:
        Вот какое решение. 1.Им сказали если задачи пересекаются по времени то не нужно их вообще никуда добавлять, даже в мапу.
        2. У них метод на проверку пересечения не приватный как у меня а публичный, вот они его и суют в хендлеры.

        Как мне поступить? не делать вообще проверку на пересечение по времени? или сделать метод публичным и не добавлять даже в мапу
        если задачи пересекаются по времени? Или вообще чтото новое придумать нужно свое?
        Ато по времени задачи даже если пересекаются у меня же задача в мапу как минимум всеровно должна попасть

        Спасибо тебе за ответы и рекомендации. Хорошего тебе дня!

         */
    }

    public void startServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/tasks", new TaskHandler()); // связываем путь и обработчик
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stopServer() throws IOException {
        httpServer.stop(1);
    }

    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
        return gson;
    }
}
