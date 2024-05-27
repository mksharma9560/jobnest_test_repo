//package com.jobnest.authms.helper;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JwtUtilSecondry {
//
//    //    private final static Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
//    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
//    private static final long EXPIRE_DURATION = 30 * 60 * 1000; // 30 min
//
//    //    private String createToken(Map<String, Object> claims, String userName) {
////        return Jwts.builder()
////                .setClaims(claims)
////                .setSubject(userName)
////                .setIssuedAt(new Date(System.currentTimeMillis()))
////                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
////                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
////    }
//    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String generateAccessToken(String username) {
//        log.info("*** Inside generateAccessToken() ***");
//        return Jwts.builder()
//                .setSubject(String.format("%s", username))
//                .setIssuer("JobNest")
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
////                .signWith(SECRET_KEY)
//                .signWith(getSignKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public boolean validateAccessToken(String token) {
//        try {
//            log.info("Executing validateAccessToken()");
//            Claims claims = getClaims(token);
//            if (claims != null && !isExpired(claims)) {
//                return true;
//            }
//        } catch (ExpiredJwtException ex) {
//            log.error("JWT expired", ex.getMessage());
//        } catch (IllegalArgumentException ex) {
//            log.error("Token is null, empty or only whitespace", ex.getMessage());
//        } catch (MalformedJwtException ex) {
//            log.error("JWT is invalid", ex);
//        } catch (UnsupportedJwtException ex) {
//            log.error("JWT is not supported", ex);
//        } catch (SignatureException ex) {
//            log.error("Signature validation failed");
//        }
//        return false;
//    }
//
//    public String getUsername(String token) {
//        Claims claims = getClaims(token);
//        log.info("*** fetched subject/username from claims: {}", claims.getSubject());
//        return claims.getSubject();
//    }
//
//    public boolean isExpired(Claims claims) {
//        Date expiration = claims.getExpiration();
//        boolean result = expiration.after(new Date(System.currentTimeMillis()));
//        if (expiration != null && result) {
//            log.info("*** Is token not expired: {}", result);
//            return false;
//        }
//        log.info("*** Is token not expired: {}", result);
//        return true;
//    }
//
////    public boolean isExpired(String token) {
////        Claims claims = getClaims(token);
////        boolean result = claims.getExpiration().after(new Date(System.currentTimeMillis()));
////        log.info("*** Is token not expired: {}", result);
////        return result;
////    }
//
//    private Claims getClaims(String token) {
//        try {
//            Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
//            if (claims != null || claims.isEmpty()) {
////        Claims body = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
//                log.info("*** Claims are: {}", claims);
//                return claims;
//            } else
//                log.info("*** Claims is NULL or Empty: {}", claims);
//            return claims;
//        } catch (JwtException ex) {
//            log.error("JWT parsing failed: {}", ex.getMessage());
//            return null;
//        }
//    }
//}