package com.hunt.worker-service-root;

import java.util.Collections;

import com.hunt.worker-service-root.business.user.entity.User;
import com.hunt.worker-service-root.business.user.entity.UserTestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import com.hunt.worker-service-root.business.user.control.TokenIssuer;

@Configuration
public class TestRestTemplateConfiguration {

    @Autowired
    private TokenIssuer tokenIssuer;

    @Autowired
    private UserTestBuilder userTestBuilder;

    @Autowired
    public void authorizedTestRestTemplate(TestRestTemplate testRestTemplate) {
        User user = userTestBuilder.build();
        String token = tokenIssuer.issueToken(user);

        testRestTemplate.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, token);
            return execution.execute(request, body);
        }));
    }
}
