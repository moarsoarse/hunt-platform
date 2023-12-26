package com.hunt.worker-service-root.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(requestHeaderAuthenticationFilter());
        http.authorizeRequests()
            .antMatchers("/webjars/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs")
            .permitAll()
            .mvcMatchers("/api/user/login", "/api/user/validate")
            .permitAll()
            .requestMatchers(EndpointRequest.to("health", "info", "prometheus", "metrics"))
            .permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint())
            .authenticated()
            .anyRequest()
            .authenticated();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().authenticationEntryPoint(http401UnauthorizedEntryPoint);
    }

    private RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setPrincipalRequestHeader(HttpHeaders.AUTHORIZATION);
        filter.setAuthenticationManager(authenticationManager());
        filter.setExceptionIfHeaderMissing(false);
        return filter;
    }

}
