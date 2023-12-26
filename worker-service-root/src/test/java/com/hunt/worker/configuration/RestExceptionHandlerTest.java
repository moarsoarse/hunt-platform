package com.hunt.worker-service-root.configuration;

import com.hunt.worker-service-root.business.common.entity.ErrorMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestExceptionHandlerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintViolation<String> constraintViolation;

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Test
    public void handleHttpMessageNotReadableException() {
        //GIVEN
        HttpMessageNotReadableException httpMessageNotReadableException = new HttpMessageNotReadableException("Exception!");

        //WHEN
        ResponseEntity<Object> responseEntity = restExceptionHandler.handleHttpMessageNotReadableException(httpMessageNotReadableException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorMessage errorMessage = (ErrorMessage) responseEntity.getBody();
        assertEquals("Unexpected error has been occur.", errorMessage.getMessage());
        assertNotNull(errorMessage.getDateTime());
        assertEquals("Exception!", errorMessage.getDescription());
        assertEquals(ExceptionUtils.getStackTrace(httpMessageNotReadableException), errorMessage.getStackTrace());
    }

    @Test
    public void handleTransactionSystemExceptionGivenUnknownRootCause() {
        //GIVEN
        TransactionSystemException transactionSystemException = new TransactionSystemException("Some unexpected exception", new RuntimeException("Runtime exception description"));

        //WHEN
        ResponseEntity<Object> responseEntity = restExceptionHandler.handleTransactionSystemException(transactionSystemException);

        //THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ErrorMessage errorMessage = (ErrorMessage) responseEntity.getBody();
        assertEquals("Unexpected error has been occur.", errorMessage.getMessage());
        assertNotNull(errorMessage.getDateTime());
        assertEquals("Some unexpected exception; nested exception is java.lang.RuntimeException: Runtime exception description", errorMessage.getDescription());
        assertEquals(ExceptionUtils.getStackTrace(transactionSystemException), errorMessage.getStackTrace());
    }

    @Test
    public void handleTransactionSystemGivenConstraintViolationExceptionRootCause() {
        //GIVEN
        when(constraintViolation.getPropertyPath().toString()).thenReturn("someField");
        when(constraintViolation.getMessage()).thenReturn("may not be null");

        Set<ConstraintViolation<String>> constraintViolations = new HashSet<>();
        constraintViolations.add(constraintViolation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException(constraintViolations);

        TransactionSystemException transactionSystemException = new TransactionSystemException("Some validation errors!", constraintViolationException);

        //WHEN
        ResponseEntity<Object> responseEntity = restExceptionHandler.handleTransactionSystemException(transactionSystemException);

        //THEN
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorMessage errorMessage = (ErrorMessage) responseEntity.getBody();

        assertEquals("Validation error!", errorMessage.getMessage());
        assertNotNull(errorMessage.getDateTime());
        assertNull(errorMessage.getDescription());
        assertNull(errorMessage.getStackTrace());
        assertEquals(1, errorMessage.getViolations().size());
        assertEquals("someField may not be null", errorMessage.getViolations().get(0));
    }
}