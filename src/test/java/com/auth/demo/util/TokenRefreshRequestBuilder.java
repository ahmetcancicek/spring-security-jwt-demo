package com.auth.demo.util;

import com.auth.demo.dto.TokenRefreshRequest;

public class TokenRefreshRequestBuilder {
    private String refreshToken;

    public TokenRefreshRequestBuilder withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public static TokenRefreshRequestBuilder generate(){
        return new TokenRefreshRequestBuilder();
    }

    public TokenRefreshRequest build() {
        return new TokenRefreshRequest(refreshToken);
    }
}
