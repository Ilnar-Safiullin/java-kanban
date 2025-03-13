package server;

import com.sun.net.httpserver.HttpServer;


import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;


    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/tasks", new TaskHandler()); // связываем путь и обработчик
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.start(); // запускаем сервер


        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");



        /*
        Сергей Привет. Я отсылаю чтобы спросить. Подскажи пожалуйста что не так с TaskHandler. Я запустил в мейне для теста
        через инсомнию через "POST" добавил таску путем <http://localhost:8080/tasks/Task1,Test> и она добавилась. Но когда
        через GET по айди пытаюсь получить эту таску или все таски, то выходит ошибка. Так как это чтото типо сервера,
        я не могу через дебаг посмотреть что не нравится ему. Притом если вобью номер айди задачи которой не существует,
        то он выдает правильно "Задача Task с таким номером id не найдена". Добавленную же задачу он находит по айди,
        но выдать почему то не может. И куратор и в чате люди тоже молчат, весь день пытаюсь это решить, даже нейросеть
        говорит все верно, до последнего не хотел тебя дергать (
        Извини если отвлекаю тебя Сергей.

         */
    }


}
