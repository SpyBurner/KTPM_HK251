package com.api_gateway.configuration;

import com.api_gateway.service.IIamService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class IamClientConfig {

    @Value("${gateway.secret-key}")
    private String gatewaySecret;

    @Bean
    public WebClient iamWebClient(WebClient.Builder builder) {
        return builder
                .defaultHeader("X-Gateway-Secret", gatewaySecret)
                .baseUrl("http://localhost:8081")
                .build();
    }

    @Bean
    public IIamService identityClient(WebClient iamWebClient) {

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(iamWebClient))
                .build();

        return factory.createClient(IIamService.class);
    }
}
