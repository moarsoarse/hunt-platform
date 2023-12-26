package com.hunt.worker-service-root.configuration;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class RestTemplateConfiguration {

    @Value("${restTemplate.connectionTimeoutInMilis}")
    private Integer restTemplateConnectionTimeout;

    @Value("${restTemplate.proxy.active:false}")
    private Boolean restTemplateProxyActive;

    @Value("${restTemplate.proxy.host}")
    private String restTemplateProxyHost;

    @Value("${restTemplate.proxy.protocol}")
    private String restTemplateProxyProtocol;

    @Value("${restTemplate.proxy.port}")
    private Integer restTemplateProxyPort;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Bean
    public RestTemplate restTemplate() {
        CloseableHttpClient httpClient = buildHttpClient();

        HttpComponentsClientHttpRequestFactory requestFactory = buildRequestFactory(httpClient);

        return buildRestTemplate(requestFactory);
    }

    private CloseableHttpClient buildHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);

        if (restTemplateProxyActive) {
            httpClientBuilder.setProxy(
                new HttpHost(restTemplateProxyHost, restTemplateProxyPort, restTemplateProxyProtocol));
        }

        return httpClientBuilder.build();
    }

    private HttpComponentsClientHttpRequestFactory buildRequestFactory(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }

    private RestTemplate buildRestTemplate(HttpComponentsClientHttpRequestFactory requestFactory) {
        return restTemplateBuilder.setConnectTimeout(restTemplateConnectionTimeout)
            .requestFactory(() -> requestFactory)
            .errorHandler(new NoOpResponseErrorHandler())
            .build();
    }

    private static class NoOpResponseErrorHandler extends DefaultResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
        }
    }
}
