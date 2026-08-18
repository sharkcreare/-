package com.pzx.knowledge.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import java.util.Date;
@Slf4j
@Component
public class JwtUtils {
    private static SecretKey STATIC_KEY;


        @Value("${jwt.secret}")
        private String secret;

    @Value("${jwt.expiration:86400000}")
        private long expiration;

        private SecretKey getKey() {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }


//            生成jwp token
    public String generateToken (Long userId,String username){

        Date now =new Date();
        Date expireTime = new Date(now.getTime()+ expiration);
        return Jwts.builder()
                .subject(username)
                .claim("userId",userId)
                .issuedAt(now)
                .expiration(expireTime)
                .signWith(getKey())
                .compact();
    }
//              解析jwp
    private Claims getClaims(String token){

            if(!StringUtils.hasText(token)){
                log.warn("token为空");
                return null;
            }
            try {
                return Jwts.parser()
                        .verifyWith(getKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
            }
            catch (ExpiredJwtException e) {
                log.error("token已过期");
            } catch (SignatureException e) {
                log.error("token签名错误，已被篡改");
            } catch (MalformedJwtException e) {
                log.error("token格式错误");
            } catch (Exception e) {
                log.error("token解析异常:{}", e.getMessage());
            }
            return null;
    }
    /**
     * 校验token是否有效
     */
    public boolean validateToken(String token) {
        return getClaims(token) != null;
    }
    /**
     * 根据token获取userId
     */
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        if (claims == null) {
            return null;
        }
        return claims.get("userId",Long.class);
    }

    @PostConstruct
    public void initStaticKey() {
        STATIC_KEY = getKey();
    }
    public static Long getUserIdFromTokenQuietly(String token) {
        if(!StringUtils.hasText(token)||STATIC_KEY==null){
                return  null;
        }
        try {
            return Jwts.parser()
                    .verifyWith(STATIC_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getPayload()
                    .get("userId", Long.class);
        }catch (ExpiredJwtException e){
            log.warn("token解析失败：{}",e.getMessage());
            return null;
        }
    }
}
