package com.vashchenko.task.controllers;

import com.vashchenko.task.dto.helpers.ExtraErrorMessage;
import com.vashchenko.task.dto.responses.ErrorResponse;
import com.vashchenko.task.exceptions.*;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ErrorHandlerController {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad request. Validation is failed");
        errorResponse.addExtra(fieldErrors.stream()
                .map(fieldError -> new ExtraErrorMessage(fieldError.getDefaultMessage(),
                        fieldError.getField(), "condition"))
                .collect(Collectors.toList()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> wrongLoginOrPasswordException(final WrongLoginOrPasswordException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> systemException(final SystemException e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> bankAccountIsNotFoundException(final BankAccountIsNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> UserIsNotFoundException(final UserIsNotFoundException e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> phoneNumberIsNotFoundException(final PhoneNumberIsNotFoundException e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> emailIsNotFoundException(final EmailIsNotFoundException e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> baseConflictException(final BaseConflictException e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT,e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> jwtException(final JwtException e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED,e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(final HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
