package com.its.iam.repository;

import com.its.iam.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Long> {
    Optional<InvalidToken> findByToken(String token);
    boolean existsByToken(String token);
}
