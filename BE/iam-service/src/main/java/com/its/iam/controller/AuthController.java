package com.its.iam.controller;

import com.its.iam.dto.request.AuthenticationRequest;
import com.its.iam.dto.request.IntrospectRequest;
import com.its.iam.dto.request.RegisterRequest;
import com.its.iam.dto.response.AuthenticationResponse;
import com.its.iam.dto.response.IntrospectResponse;
import com.its.iam.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.its.iam.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationService authenticationService;

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request){
        return ApiResponse.<IntrospectResponse>builder().result(authenticationService.introspect(request)).build();
    }
    
    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.register(request)).build();
    }
    
    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.authenticate(request)).build();
    }
    
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.refreshToken(authHeader)).build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        return ApiResponse.<String>builder().result(authenticationService.logout(request)).build();
    }
}