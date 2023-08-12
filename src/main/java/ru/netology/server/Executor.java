package ru.netology.server;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import ru.netology.server.dto.Request;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

class Executor implements Runnable {

    private final Socket socket;
    private final Map<Request, Handler> handlers;

    public Executor(Socket socket, Map<Request, Handler> handlers) {
        this.socket = socket;
        this.handlers = handlers;
    }

    @Override
    public void run() {
        try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final var out = new BufferedOutputStream(socket.getOutputStream())) {
            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                // just close socket
                socket.close();
            }

            String[] pathParts = parts[1].split("\\?");

            Request request;
            if (pathParts.length == 2) {
                List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(pathParts[1], StandardCharsets.UTF_8);

                request = new Request(parts[0], pathParts[0], nameValuePairs);
            } else {
                request = new Request(parts[0], pathParts[0]);
            }

            if (handlers.containsKey(request)) {
                request.getQueryParams().forEach(param -> System.out.println(param.getName() + ": " + param.getValue()));
                handlers.get(request).handle(request, out);
            } else {
                out.write(("HTTP/1.1 404 Not Found\r\n" + "Content-Length: 0\r\n" + "Connection: close\r\n" + "\r\n").getBytes());
                out.flush();
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
