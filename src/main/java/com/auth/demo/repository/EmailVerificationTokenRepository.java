package com.auth.demo.repository;

import com.auth.demo.model.EmailVerificationToken;
import com.auth.demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends CrudRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);
}
