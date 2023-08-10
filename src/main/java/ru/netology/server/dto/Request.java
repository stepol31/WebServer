package ru.netology.server.dto;

import java.util.Objects;

public class Request {
    String method;
    String path;

    public Request(String method, String path) {
        this.method = method;
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return method.equals(request.method) && path.equals(request.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
