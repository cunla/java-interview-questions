package com.example.demo.exception;

import com.example.demo.entity.HolidayError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String VALIDATION_ERROR_MESSAGE = "parameter is invalid. Date must be yyyy-MM-dd format !";
    private final static String VALIDATION_MISSING_ERROR_MESSAGE = "parameter is missing. Country must be entered !";

    @ExceptionHandler(HolidayApiException.class)
    protected ResponseEntity<HolidayError> handleApiException(HolidayApiException exception) {
        return ResponseEntity.status(exception.getError().getStatus()).body(exception.getError());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        ValidationExceptionMessage message = new ValidationExceptionMessage(
                ex.getParameterName(),
                ex.getParameterType(),
                VALIDATION_MISSING_ERROR_MESSAGE
        );
        return ResponseEntity.status(status).body(message);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentException(MethodArgumentTypeMismatchException ex) {
        ValidationExceptionMessage message = new ValidationExceptionMessage(
                ex.getParameter().getParameterName(),
                Objects.requireNonNull(ex.getValue()).toString(),
                VALIDATION_ERROR_MESSAGE
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @Getter
    @AllArgsConstructor
    static class ValidationExceptionMessage {
        private String filed;
        private Object rejectedValue;
        private String message;
    }
}


