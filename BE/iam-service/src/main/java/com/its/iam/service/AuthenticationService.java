package com.its.iam.service;

import com.its.iam.dto.*;
import com.its.iam.entity.User;
import com.its.iam.exception.AuthenticationException;
import com.its.iam.exception.InvalidTokenException;
import com.its.iam.mapper.UserMapper;
import com.its.iam.repository.UserRepository;
import com.its.iam.security.IJwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;
    private final ITokenService tokenService;
    private final UserMapper userMapper;


    public AuthenticationResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Email already exists");
        }
        
        // Tạo User với mã hóa password
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .displayName(request.getDisplayName())
                .role(request.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        var savedUser = userRepository.save(user);

        // Generate JWT tokens
        TokenPair tokenPair = tokenService.generateToken(savedUser);

        // Tạo response
        return AuthenticationResponse.builder()
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .userDto(userMapper.mapToUserDto(savedUser))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Nguoi dung khong ton tai"));
        UserDto userDto = userMapper.mapToUserDto(user);
        TokenPair tokenPair = tokenService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .userDto(userDto).build();

    }

    public AuthenticationResponse refreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Missing or invalid Authorization header");
        }

        String refreshToken = authHeader.substring(7);

        // Validate token → your logic here
        String username = jwtService.extractUsername(refreshToken);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Nguoi dung khong ton tai"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new AuthenticationException("Invalid refresh token");
        }
        UserDto userDto = userMapper.mapToUserDto(user);
        TokenPair pair = tokenService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(pair.accessToken())
                .refreshToken(pair.refreshToken())
                .userDto(userDto)
                .build();
    }

    public String logout(HttpServletRequest request) {
        // 1. Extract token from header
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Invalid token format");
        }

        final String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);


        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Nguoi dung khong ton tai"));

        // 2. Validate token before blacklisting
        try {
            // Verify token is valid (not expired, valid signature)
            if (!jwtService.isTokenValid(jwt, user)) {
                throw new InvalidTokenException("Token is invalid or expired");
            }

            // 3. Check if token is already blacklisted
            if (tokenService.isInvalidToken(jwt)) {
                return "Already logged out";
            }

            // 5. Blacklist the token
            tokenService.setTokenInvalid(jwt);

            // Note: No need for SecurityContextHolder.clearContext()
            // in stateless JWT auth - context is per-request

            return "Logged out successfully";

        } catch (ExpiredJwtException e) {
            // Token already expired - no need to blacklist
            throw new InvalidTokenException("Token has already expired");
        } catch (JwtException e) {
            throw new InvalidTokenException("Invalid token");
        }
    }


}
