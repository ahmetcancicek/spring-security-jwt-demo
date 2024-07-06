package com.auth.demo.handler;

import com.auth.demo.controller.BaseController;
import com.auth.demo.response.ErrorResponse;
import com.auth.demo.response.Response;
import com.auth.demo.exception.AlreadyExistsException;
import com.auth.demo.exception.BusinessException;
import com.auth.demo.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response<ErrorResponse> handleException(Exception exception, Locale locale) {
        logger.error("An error occurred!: ", exception);
        return createErrorResponseFromMessageSource("system.error.occurred", locale);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<ErrorResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, Locale locale) {
        return createErrorResponseFromMessageSource("client.methodNotSupported", locale, exception.getMethod());
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(DisabledException.class)
    public Response<ErrorResponse> handleDisabledException(DisabledException exception, Locale locale) {
        return respond(new ErrorResponse(HttpStatus.FORBIDDEN.toString(), exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<ErrorResponse> handleInvalidArgumentException(MethodArgumentNotValidException exception) {
        // TODO: Add the field errors
        return respond(new ErrorResponse("", ""));
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(BusinessException.class)
    public Response<ErrorResponse> handleBusinessException(BusinessException exception, Locale locale) {
        return createErrorResponseFromMessageSource(exception.getKey(), locale, exception.getArgs());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Response<ErrorResponse> handleNotFoundException(NotFoundException exception, Locale locale) {
        return createErrorResponseFromMessageSource(exception.getKey(), locale, exception.getArgs());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistsException.class)
    public Response<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException exception, Locale locale) {
        return createErrorResponseFromMessageSource(exception.getKey(), locale, exception.getArgs());
    }

    public Response<ErrorResponse> createErrorResponseFromMessageSource(String key, Locale locale, String... args) {
        List<String> messages = retrieveLocalizationMessage(key, locale, args);
        return respond(new ErrorResponse(messages.get(0), messages.get(1)));
    }

    private List<String> retrieveLocalizationMessage(String key, Locale locale, String... args) {
        String message = messageSource.getMessage(key, args, locale);
        return Pattern.compile(";").splitAsStream(message).collect(Collectors.toList());
    }
}
