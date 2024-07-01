package com.auth.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtValidator(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtDecoder.decode(token).getSubject();
        } catch (JwtException ex) {
            throw new JwtException("JWT: " + token + "Unsupported JWT token");
        } catch (Exception ex) {
            throw new JwtException("JWT: " + token + "Unsupported JWT token");
        }
        return true;
    }
}
