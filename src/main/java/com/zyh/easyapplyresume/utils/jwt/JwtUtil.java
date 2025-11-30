package com.zyh.easyapplyresume.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT工具类
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class JwtUtil {
    public String generateToken(Integer userId,String userName,String userType,String secret,long expireTime){
        ConcurrentHashMap<String,Object> claims = new ConcurrentHashMap<>();
        claims.put("userId",userId);
        claims.put("userName",userName);
        claims.put("userType",userType);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token,String secret){
        try{
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (ExpiredJwtException e) {
            log.warn("Token 已过期: {}", token);
            throw new RuntimeException("Token 已过期");
        } catch (JwtException e) {
            log.error("Token 解析失败: {}", e.getMessage());
            throw new RuntimeException("Token 无效");
        }
    }

    public Integer getUserIdFromToken(String token, String secret) {
        Claims claims = parseToken(token, secret);
        return claims.get("userId", Integer.class);
    }

    public String getUsernameFromToken(String token, String secret) {
        Claims claims = parseToken(token, secret);
        return claims.getSubject();
    }

    public boolean validateToken(String token, String secret) {
        try {
            parseToken(token, secret);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
