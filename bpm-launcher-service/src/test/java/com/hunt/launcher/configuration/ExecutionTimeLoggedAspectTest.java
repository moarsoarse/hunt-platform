package com.hunt.bpm-launcher-service.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ExecutionTimeLoggedAspectTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private Signature signature;

    @InjectMocks
    private ExecutionTimeLoggedAspect executionTimeLoggedAspect;

    @BeforeEach
    public void setUp() throws Exception {
        doReturn(signature).when(proceedingJoinPoint).getSignature();
        doReturn(ExecutionTimeLoggedAspectTest.class).when(signature).getDeclaringType();

        doReturn("SignatureName").when(signature).getName();
    }

    @Test
    public void timeWhenNoException() throws Throwable {
        //GIVEN

        //WHEN
        executionTimeLoggedAspect.time(proceedingJoinPoint);

        //THEN
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    public void timeWhenException() throws Throwable {
        //GIVEN
        doThrow(Throwable.class).when(proceedingJoinPoint).proceed();

        //WHEN
        try {
            executionTimeLoggedAspect.time(proceedingJoinPoint);
        } catch (Throwable e) {
            //THEN
            verify(proceedingJoinPoint, times(1)).proceed();
        }
    }
}