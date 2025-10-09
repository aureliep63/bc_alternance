package com.example.BC_alternance.controller.rest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApplicationRestControllerAdvice {

    // cette méthode sera invoquée par la DispatcherServlet
   // gérer les erreurs de validation
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    @ResponseStatus(code= HttpStatus.BAD_REQUEST)
    public List<String> traiterConstraintViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
    }
}
