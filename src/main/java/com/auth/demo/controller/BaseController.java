package com.auth.demo.controller;

import com.auth.demo.response.ErrorResponse;
import com.auth.demo.response.Response;
import com.auth.demo.response.ResponseBuilder;

public class BaseController {

    protected <T> Response<T> respond(T item) {
        return ResponseBuilder.build(item);
    }

    protected Response<ErrorResponse> respond(ErrorResponse errorResponse) {
        return ResponseBuilder.build(errorResponse);
    }
}
