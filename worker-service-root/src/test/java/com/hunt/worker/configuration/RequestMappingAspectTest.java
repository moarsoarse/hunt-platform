package com.hunt.worker-service-root.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RequestMappingAspectTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Spy
    @InjectMocks
    private RequestMappingAspect requestMappingAspect;

    @BeforeEach
    public void setUp() {
        doReturn(httpServletRequest).when(requestMappingAspect).getHttpServletRequest();
    }

    @Test
    public void logWhenNoException() throws Throwable {
        //GIVEN

        //WHEN
        requestMappingAspect.log(proceedingJoinPoint);

        //THEN
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    public void logWhenException() throws Throwable {
        //GIVEN
        doThrow(Throwable.class).when(proceedingJoinPoint).proceed();

        //WHEN
        try {
            requestMappingAspect.log(proceedingJoinPoint);
        } catch (Throwable e) {
            //THEN
            verify(proceedingJoinPoint, times(1)).proceed();
        }
    }


}