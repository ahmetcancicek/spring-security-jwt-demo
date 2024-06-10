package com.auth.demo.repository;

import com.auth.demo.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
