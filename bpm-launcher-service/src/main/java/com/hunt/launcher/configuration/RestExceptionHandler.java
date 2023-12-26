package com.hunt.bpm-launcher-service.configuration;

import com.hunt.bpm-launcher-service.business.common.entity.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler  {

    @ExceptionHandler
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.warn("Returning HTTP 400 Bad Request", exception);

        ErrorMessage errorMessage = ErrorMessage.builder()
            .message("Unexpected error has been occur.")
            .dateTime(LocalDateTime.now())
            .description(exception.getMessage())
            .stackTrace(ExceptionUtils.getStackTrace(exception))
            .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException exception) {
        if (exception.getRootCause() instanceof ConstraintViolationException) {
            return handleConstraintViolationException(exception);
        } else {
            return handleGeneralTransactionSystemException(exception);
        }
    }

    private ResponseEntity<Object> handleConstraintViolationException(TransactionSystemException exception) {
        Set<ConstraintViolation<?>> violations = getViolations(exception);

        ErrorMessage errorMessage = buildErrorMessage(violations);

        log.warn("Constraint violation exception = {}!", errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private Set<ConstraintViolation<?>> getViolations(TransactionSystemException exception) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception.getRootCause();
        return constraintViolationException.getConstraintViolations();
    }

    private ErrorMessage buildErrorMessage(Set<ConstraintViolation<?>> constraintViolations) {
        List<String> violations = constraintViolations.stream().map(violation -> violation.getPropertyPath().toString() + " " + violation.getMessage()).collect(Collectors.toList());

        return ErrorMessage.builder()
            .message("Validation error!")
            .dateTime(LocalDateTime.now())
            .violations(violations)
            .build();
    }

    private ResponseEntity<Object> handleGeneralTransactionSystemException(TransactionSystemException exception) {
        ErrorMessage errorMessage = ErrorMessage.builder()
            .message("Unexpected error has been occur.")
            .dateTime(LocalDateTime.now())
            .description(exception.getMessage())
            .stackTrace(ExceptionUtils.getStackTrace(exception))
            .build();

        log.warn("Unexpected exception = {}!", errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
