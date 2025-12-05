package com.its.iam.service;

import com.its.iam.dto.TokenPair;
import com.its.iam.entity.InvalidToken;
import com.its.iam.entity.User;
import com.its.iam.repository.InvalidTokenRepository;
import com.its.iam.security.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{
    private final IJwtService jwtService;
    private final InvalidTokenRepository invalidTokenRepository;

    @Override
    public void setTokenInvalid(String token){
        InvalidToken invalidToken = InvalidToken.builder()
                .token(token)
                .invokedTime(Instant.now())
                .build();
        invalidTokenRepository.save(invalidToken);
    }

    @Override
    public boolean isInvalidToken(String token) {
        return invalidTokenRepository.existsByToken(token);
    }

    @Override
    public TokenPair generateToken(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new TokenPair(accessToken, refreshToken);
    }

}
