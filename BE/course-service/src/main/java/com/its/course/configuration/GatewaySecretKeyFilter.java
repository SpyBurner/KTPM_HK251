package com.its.course.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class GatewaySecretKeyFilter extends OncePerRequestFilter {

    private static final String GATEWAY_HEADER = "X-Gateway-Secret";
    private final String expectedSecretKey;

    public GatewaySecretKeyFilter(String expectedSecretKey) {
        this.expectedSecretKey = expectedSecretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String headerValue = request.getHeader(GATEWAY_HEADER);

        log.error(headerValue);
        if (headerValue == null || !headerValue.equals(expectedSecretKey)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid or missing gateway secret key");
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("gateway", null, List.of())
        );
        filterChain.doFilter(request, response);
    }
}