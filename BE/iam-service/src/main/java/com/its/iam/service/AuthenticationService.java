package com.its.iam.service;

import com.its.iam.dto.request.AuthenticationRequest;
import com.its.iam.dto.request.IntrospectRequest;
import com.its.iam.dto.request.RegisterRequest;
import com.its.iam.dto.response.AuthenticationResponse;
import com.its.iam.dto.response.IntrospectResponse;
import com.its.iam.dto.response.TokenPair;
import com.its.iam.dto.response.UserDto;
import com.its.iam.entity.Role;
import com.its.iam.entity.User;
import com.its.iam.exception.AppException;
import com.its.iam.exception.ErrorCode;
import com.its.iam.mapper.UserMapper;
import com.its.iam.repository.RoleRepository;
import com.its.iam.repository.UserRepository;
import com.its.iam.security.IJwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;
    private final ITokenService tokenService;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getToken();
        boolean isValid = true;

        String username = "";
        Long roleId = null;
        try {
            username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            roleId = user.getRole() != null ? user.getRole().getId() : null;
            if (!jwtService.isTokenValid(token, user)) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }

            if (tokenService.isInvalidToken(token)) {
                throw new AppException(ErrorCode.TOKEN_ALREADY_BLACKLISTED);
            }

        } catch (ExpiredJwtException e) {
            log.warn("Token expired during introspection", e);
            isValid = false;
        } catch (JwtException e) {
            log.warn("Invalid JWT token during introspection", e);
            isValid = false;
        } catch (AppException e) {
            log.warn("Introspection failed: {}", e.getMessage());
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .username(username)
                .roleId(roleId)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Role role = null;

        if (request.getRoleId() != null) {
            role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .displayName(request.getDisplayName())
                .role(role)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        // Generate JWT tokens
        TokenPair tokenPair = tokenService.generateToken(savedUser);

        // Build response
        return AuthenticationResponse.builder()
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .userDto(userMapper.mapToUserDto(savedUser))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials for user: {}", request.getUsername());
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Check if account is active (handle null case)
        if (user.getActive() == null || !user.getActive()) {
            throw new AppException(ErrorCode.ACCOUNT_INACTIVE);
        }

        log.info("User authenticated successfully: {}", user.getUsername());

        UserDto userDto = userMapper.mapToUserDto(user);
        TokenPair tokenPair = tokenService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .userDto(userDto)
                .build();
    }

    public AuthenticationResponse refreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.MISSING_TOKEN);
        }

        String refreshToken = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(refreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            if (!jwtService.isTokenValid(refreshToken, user)) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }

            if (tokenService.isInvalidToken(refreshToken)) {
                throw new AppException(ErrorCode.TOKEN_ALREADY_BLACKLISTED);
            }

            log.info("Token refreshed successfully for user: {}", username);

            UserDto userDto = userMapper.mapToUserDto(user);
            TokenPair pair = tokenService.generateToken(user);

            return AuthenticationResponse.builder()
                    .accessToken(pair.accessToken())
                    .refreshToken(pair.refreshToken())
                    .userDto(userDto)
                    .build();

        } catch (ExpiredJwtException e) {
            log.warn("Expired refresh token", e);
            throw new AppException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            log.warn("Invalid JWT refresh token", e);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String logout(HttpServletRequest request) {
        // Extract token from header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.MISSING_TOKEN);
        }

        String jwt = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(jwt);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            // Validate token before blacklisting
            if (!jwtService.isTokenValid(jwt, user)) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }

            // Check if token is already blacklisted
            if (tokenService.isInvalidToken(jwt)) {
                throw new AppException(ErrorCode.TOKEN_ALREADY_BLACKLISTED);
            }

            // Blacklist the token
            tokenService.setTokenInvalid(jwt);
            log.info("User logged out successfully: {}", username);

            return "Logged out successfully";

        } catch (ExpiredJwtException e) {
            log.warn("Attempted to logout with expired token", e);
            throw new AppException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            log.warn("Invalid JWT token during logout", e);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}