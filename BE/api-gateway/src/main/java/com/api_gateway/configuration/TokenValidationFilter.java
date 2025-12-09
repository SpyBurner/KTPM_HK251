package com.api_gateway.configuration;

import com.api_gateway.dto.request.IntrospectRequest;
import com.api_gateway.dto.response.IntrospectResponse;
import com.api_gateway.exception.AppException;
import com.api_gateway.exception.ErrorCode;
import com.api_gateway.service.IIamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
public class TokenValidationFilter implements GlobalFilter, Ordered {

    private final IIamService iamService;
    private final String gatewaySecret;

    private final static String[] PUBLIC_ENDPOINTS = {
            "/api/iam-service/auth/**",
            "/api/iam-service/auth/introspect",
            "/api/iam-service/auth/authenticate",
            "/v3/api-docs/",
            "/swagger-ui/",
            "/swagger-ui.html"
    };

    private boolean isPublic(String path) {
        for (String pattern : PUBLIC_ENDPOINTS) {
            if (match(pattern, path))
                return true;
        }
        return false;
    }

    private boolean match(String pattern, String path) {
        String regex = pattern
                .replace(".", "\\.")
                .replace("**", ".*")
                .replace("*", "[^/]*");

        return path.matches(regex);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        // ================ PUBLIC PATH ================

        if (isPublic(path)) {
            ServerWebExchange mutated = exchange.mutate()
                    .request(builder -> builder.header("X-Gateway-Secret", gatewaySecret))
                    .build();

            return chain.filter(mutated);
        }

        // ================ PRIVATE PATH ================
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (auth == null || !auth.startsWith("Bearer ")) {
            return Mono.error(new AppException(ErrorCode.UNAUTHENTICATED));
        }

        String token = auth.substring(7);
        return iamService.introspect(new IntrospectRequest(token))
                .flatMap(api -> {

                    if (api == null || api.getResult() == null || !api.getResult().isValid()) {
                        return Mono.error(new AppException(ErrorCode.UNAUTHENTICATED));
                    }
                    IntrospectResponse user = api.getResult();
                    ServerWebExchange mutated = exchange.mutate()
                            .request(builder -> {
                                builder.header("X-User-Id", user.getUsername());
                                builder.header("X-User-Roles", user.getRoleId().toString());
                                builder.header("X-Gateway-Secret", gatewaySecret);
                            })
                            .build();

                    return chain.filter(mutated);
                })
                .onErrorResume(e -> Mono.error(new AppException(ErrorCode.UNAUTHENTICATED)));
    }
}
