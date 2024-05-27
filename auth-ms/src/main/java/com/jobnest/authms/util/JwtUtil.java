package com.jobnest.authms.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private static final long EXPIRE_DURATION = 30 * 60 * 1000;

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String username) {
        log.info("Executing generateAccessToken()");
        return Jwts.builder()
                .setSubject(String.format("%s", username))
                .setIssuer("JobNest")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            log.info("Executing validateAccessToken()");
            Claims claims = getClaims(token);
            if (claims != null && !isExpired(claims)) {
                return true;
            }
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Token is invalid: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Token not supported: {}", ex.getMessage());
        } catch (SignatureException ex) {
            log.error("Token Signature validation failed: {}", ex.getMessage());
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public boolean isExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        boolean result = expiration.after(new Date(System.currentTimeMillis()));
        if (result) {
            log.info("Token not expired: {}", result);
            return false;
        }
        return true;
    }

    private Claims getClaims(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
            if (claims != null || claims.isEmpty()) {
                return claims;
            } else
                return claims;
        } catch (JwtException ex) {
            log.error("JWT parsing failed: {}", ex.getMessage());
            return null;
        }
    }
}