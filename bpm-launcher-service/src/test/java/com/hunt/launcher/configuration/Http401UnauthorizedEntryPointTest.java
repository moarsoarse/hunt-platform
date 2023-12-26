package com.hunt.bpm-launcher-service.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class Http401UnauthorizedEntryPointTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private AuthenticationException authenticationException;

    @InjectMocks
    private Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;

    @Test
    public void commence() throws IOException, ServletException {
        //WHEN
        http401UnauthorizedEntryPoint.commence(httpServletRequest, httpServletResponse, authenticationException);

        //THEN
        verify(httpServletResponse, only()).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied, your token has been expired or you should provide valid JWT token.");
    }
}