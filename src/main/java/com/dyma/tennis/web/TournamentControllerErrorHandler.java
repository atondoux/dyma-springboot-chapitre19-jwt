package com.dyma.tennis.web;

import com.dyma.tennis.model.Error;
import com.dyma.tennis.service.TournamentAlreadyExistsException;
import com.dyma.tennis.service.TournamentDataRetrievalException;
import com.dyma.tennis.service.TournamentNotFoundException;
import com.dyma.tennis.service.TournamentRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TournamentControllerErrorHandler {

    @ExceptionHandler(TournamentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleTournamentNotFoundException(TournamentNotFoundException ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(TournamentAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleTournamentAlreadyExistsException(TournamentAlreadyExistsException ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler(TournamentDataRetrievalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleTournamentDataRetrievalException(TournamentDataRetrievalException ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler(TournamentRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleTournamentRegistrationException(TournamentRegistrationException ex) {
        return new Error(ex.getMessage());
    }
}
