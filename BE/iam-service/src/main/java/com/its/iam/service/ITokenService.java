package com.its.iam.service;

import com.its.iam.dto.response.TokenPair;
import com.its.iam.entity.User;

public interface ITokenService {
    void setTokenInvalid(String token);
    boolean isInvalidToken(String token);
    TokenPair generateToken(User user);
}
