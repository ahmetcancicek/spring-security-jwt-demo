package com.auth.demo.response;

public class ResponseBuilder {
    public ResponseBuilder() {

    }

    public static <T> Response<T> build(T item) {
        return new Response<>(item);
    }

    public static Response<ErrorResponse> build(ErrorResponse errorResponse) {
        return new Response<>(errorResponse);
    }
}
