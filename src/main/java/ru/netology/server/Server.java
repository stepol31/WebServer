package ru.netology.server;

import ru.netology.server.dto.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private final ExecutorService executorService = Executors.newFixedThreadPool(64);
    private final static Map<Request, Handler> handlers = new HashMap<>();

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();

                Executor executor = new Executor(socket, handlers);
                executorService.submit(executor);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addHandler(String method, String path, Handler handler) {
        Request key = new Request(method, path);
        if (handlers.containsKey(key)) {
            throw new RuntimeException("Handler with same method and path already registered");
        }
        handlers.put(key, handler);
    }
}

