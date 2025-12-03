package com.api_gateway.configuration;

import java.time.Instant;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import com.api_gateway.exception.AppException;
import com.api_gateway.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomJwtDecoder implements ReactiveJwtDecoder {

    @Value("${jwt.secret}")
    private String signerKey;

    @Value("${jwt.algorithm:HS512}")
    private String algorithm;

    private volatile NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        return Mono.fromCallable(() -> {
            try {
                log.debug("Starting JWT validation for token");

                Jwt jwt = getDecoder().decode(token);

                validateTokenExpiration(jwt);
                validateTokenClaims(jwt);

                log.debug("JWT validation successful for subject: {}", jwt.getSubject());
                return jwt;

            } catch (JwtException e) {
                log.error("JWT validation failed: {}", e.getMessage());
                throw e;
            } catch (Exception e) {
                log.error("Unexpected error during JWT validation: {}", e.getMessage());
                throw new JwtException("Token validation failed", e);
            }
        });
    }

    private NimbusJwtDecoder getDecoder() {
        if (Objects.isNull(nimbusJwtDecoder)) {
            synchronized (this) {
                if (Objects.isNull(nimbusJwtDecoder)) {
                    nimbusJwtDecoder = buildDecoder();
                }
            }
        }
        return nimbusJwtDecoder;
    }

    private NimbusJwtDecoder buildDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                signerKey.getBytes(),
                algorithm
        );

        MacAlgorithm macAlgorithm = MacAlgorithm.from(algorithm);

        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(macAlgorithm)
                .build();
    }

    private void validateTokenExpiration(Jwt jwt) {
        Instant expiresAt = jwt.getExpiresAt();
        if (expiresAt == null) {
            log.error("Token has no expiration time");
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        if (expiresAt.isBefore(Instant.now())) {
            log.error("Token has expired at: {}", expiresAt);
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    private void validateTokenClaims(Jwt jwt) {
        if (jwt.getSubject() == null || jwt.getSubject().isEmpty()) {
            log.error("Token has no subject");
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }
}