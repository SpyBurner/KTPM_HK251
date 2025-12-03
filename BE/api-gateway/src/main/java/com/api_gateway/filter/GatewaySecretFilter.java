package com.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GatewaySecretFilter implements GlobalFilter, Ordered {

    @Value("${gateway.secret-key:your-secret-gateway-key-change-this}")
    private String gatewaySecretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // ✅ LOG TRƯỚC KHI MUTATE
        log.error(">>> [BEFORE FORWARD] Path = {}", path);
        log.error(">>> [BEFORE FORWARD] Old X-Gateway-Secret = {}",
                exchange.getRequest().getHeaders().getFirst("X-Gateway-Secret"));

        // ✅ ADD HEADER
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-Gateway-Secret", gatewaySecretKey)
                .build();

        // ✅ LOG SAU KHI MUTATE
        log.error(">>> [AFTER MUTATE] X-Gateway-Secret = {}",
                request.getHeaders().getFirst("X-Gateway-Secret"));

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request)
                .build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
