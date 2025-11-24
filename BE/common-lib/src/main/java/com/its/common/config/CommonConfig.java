package com.its.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Common configuration for all services
 */
@Configuration
public class CommonConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}