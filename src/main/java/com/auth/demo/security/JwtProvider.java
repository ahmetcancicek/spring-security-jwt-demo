package com.auth.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${app.token.expiration}")
    private Long jwtExpirationInMs;


    public JwtProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(AuthUser authUser) {
        // Convert milliseconds to Duration
        Duration duration = Duration.ofMillis(jwtExpirationInMs);
        // Add duration to current instant
        Instant futureInstant = Instant.now().plus(duration);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(futureInstant)
                .subject(authUser.getUser().getUsername())
                .claim("authorities", getUserAuthorities(authUser))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String getUsernameFromJWT(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwtDecoder.decode(token).getSubject();
    }

    public List<GrantedAuthority> getAuthoritiesFromJWT(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        Map<String, Object> claims = jwt.getClaims();
        return Arrays.stream(claims.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private String getUserAuthorities(AuthUser authUser) {
        return authUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public long getExpiryDuration() {
        return jwtExpirationInMs;
    }
}
