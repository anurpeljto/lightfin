package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.user.User;

import java.util.Map;

public interface JwtService {

    public String extractEmail(String token);

    public String extractId(String token);

    public boolean isTokenValid(String token, User user);

    public String generateToken(Map<String, Object> extraClaims, User user);

    public String generateToken(User user);
}
