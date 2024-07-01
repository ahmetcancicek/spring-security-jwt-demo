package com.auth.demo.security;

import com.auth.demo.service.UserDetailsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;


    public JwtProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(Authentication authentication) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(10, ChronoUnit.HOURS))
                .subject(((AuthUser) authentication.getPrincipal()).getUser().getUsername())
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
}
