package com.auth.demo.response;

public class Response<T> {
    private T data;
    private ErrorResponse errors;

    public Response() {

    }

    public Response(ErrorResponse errors) {
        this.errors = errors;
    }

    public Response(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
