package ru.netology;

import ru.netology.server.Handler;
import ru.netology.server.Server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        Handler defaultHandler = (request, outputStream) -> {

            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);

            final var length = Files.size(filePath);
            outputStream.write(("HTTP/1.1 200 OK\r\n" + "Content-Type: " + mimeType + "\r\n" + "Content-Length: " + length + "\r\n" + "Connection: close\r\n" + "\r\n").getBytes());
            Files.copy(filePath, outputStream);
            outputStream.flush();
        };
        server.addHandler("GET", "/index.html", defaultHandler);
        server.addHandler("GET", "/spring.svg", defaultHandler);
        server.addHandler("GET", "/spring.png", defaultHandler);
        server.addHandler("GET", "/resources.html", defaultHandler);
        server.addHandler("GET", "/styles.css", defaultHandler);
        server.addHandler("GET", "/app.js", defaultHandler);
        server.addHandler("GET", "/links.html", defaultHandler);
        server.addHandler("GET", "/forms.html", defaultHandler);
        server.addHandler("GET", "/events.html", defaultHandler);
        server.addHandler("GET", "/events.js", defaultHandler);

        server.addHandler("GET", "/classic.html", (request, outputStream) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);

            final var template = Files.readString(filePath);
            final var content = template.replace("{time}", LocalDateTime.now().toString()).getBytes();
            outputStream.write(("HTTP/1.1 200 OK\r\n" + "Content-Type: " + mimeType + "\r\n" + "Content-Length: " + content.length + "\r\n" + "Connection: close\r\n" + "\r\n").getBytes());
            outputStream.write(content);
            outputStream.flush();

        });

        server.listen(9999);
    }
}


