package com.auth.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "app.token.rsa")
public record RSAKeyConfigProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
