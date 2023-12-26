package com.hunt.bpm-launcher-service.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class RestTemplateConfigurationTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestTemplateBuilder restTemplateBuilder;

    @InjectMocks
    private RestTemplateConfiguration restTemplateConfiguration;

    @BeforeEach
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(restTemplateConfiguration, "restTemplateConnectionTimeout", 2000);
        ReflectionTestUtils.setField(restTemplateConfiguration, "restTemplateProxyActive", true);
        ReflectionTestUtils.setField(restTemplateConfiguration, "restTemplateProxyHost", "localhost");
        ReflectionTestUtils.setField(restTemplateConfiguration, "restTemplateProxyProtocol", "http");
        ReflectionTestUtils.setField(restTemplateConfiguration, "restTemplateProxyPort", 9988);
    }

    @Test
    public void restTemplateWhenProxyInactive() {
        //GIVEN
        ReflectionTestUtils.setField(restTemplateConfiguration, "restTemplateProxyActive", false);

        //WHEN
        restTemplateConfiguration.restTemplate();
    }

    @Test
    public void restTemplateWhenProxyActive() {
        //GIVEN
        ReflectionTestUtils.setField(restTemplateConfiguration, "restTemplateProxyActive", true);

        //WHEN
        restTemplateConfiguration.restTemplate();
    }
}