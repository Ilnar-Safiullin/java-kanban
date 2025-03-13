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


        File file = new File("./resources/task.csv");
        FileBackedTaskManager managerRestored = new FileBackedTaskManager(file);
        System.out.println(managerRestored.getTasks());

        Epic epic1 = new Epic("Test 9", "Test 9");
        managerRestored.addInMapEpic(epic1);
        Subtask subtask1 = new Subtask("Test 9", "Test 9", epic1.getId(), Duration.ofMinutes(10), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Test 9", "Test 9", epic1.getId(), Duration.ofMinutes(10), LocalDateTime.now().minusMinutes(15L));
        managerRestored.addInMapSubtask(subtask1);
        managerRestored.addInMapSubtask(subtask2);
        System.out.println(managerRestored.getTasks());
        System.out.println("===============");
         */
    }





}
