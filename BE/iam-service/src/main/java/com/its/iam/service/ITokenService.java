package com.its.iam.service;

import com.its.iam.dto.TokenPair;
import com.its.iam.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface ITokenService {
    void setTokenInvalid(String token);
    boolean isInvalidToken(String token);
    TokenPair generateToken(User user);
}
