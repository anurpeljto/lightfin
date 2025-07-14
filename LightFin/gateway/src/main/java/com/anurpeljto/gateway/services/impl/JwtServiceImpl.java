package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.user.User;
import com.anurpeljto.gateway.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${lightfin.security.secret}")
    protected String signInKey;

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractId(String token) {
        return extractClaim(token, claims -> {
            Integer idInt = claims.get("id", Integer.class);
            return idInt != null ? idInt.toString() : null;
        });
    }

    @Override
    public String generateToken(User user){
        return generateToken(new HashMap<>(), user);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signInKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(
            Map<String, Object> extraClaims,
            User userDetails
    ) {
        extraClaims.put("id", userDetails.getId());
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getEmail()).issuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public Date extractExpiration(String token){
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration;
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    @Override
    public boolean isTokenValid(String token, User userDetails){
        final String email = extractEmail(token);
        return email.equals(userDetails.getEmail()) && !isTokenExpired(token);
    }
}
