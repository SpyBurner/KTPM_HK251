package com.its.learning.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
public class FeignClientConfig implements RequestInterceptor {

    private static final String GATEWAY_HEADER = "X-Gateway-Secret";

    @Override
    public void apply(RequestTemplate template) {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            // Forward Authorization token
            String token = request.getHeader("Authorization");
            if (token != null) {
                log.debug("Forwarding Authorization token to Feign: {}", token);
                template.header("Authorization", token);
            }

            // Forward Gateway Secret Header
            String gatewaySecret = request.getHeader(GATEWAY_HEADER);
            if (gatewaySecret != null) {
                log.debug("Forwarding gateway secret header to Feign");
                template.header(GATEWAY_HEADER, gatewaySecret);
            }
        }
    }
}