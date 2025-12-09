package com.its.iam.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface IJwtService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    <T> T extractAllClaim(String token, Function<Claims, T> claimsResolver);
    boolean isTokenValid(String token, UserDetails userDetails);
}
