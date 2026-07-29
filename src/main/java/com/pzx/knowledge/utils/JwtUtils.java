package com.pzx.knowledge.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import java.util.Date;
@Slf4j
@Component
public class JwtUtils {
    private static final String SECRET ="your-256-bit-secret-key-here-make-it-long";
    private static final long  EXPIRATION =1000 * 60 * 60 * 24;


    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

//            生成jwp
    public String generateToken (Long userId,String username){

        Date now =new Date();
        Date expiration = new Date(now.getTime()+EXPIRATION);
        return Jwts.builder()
                .subject(username)
                .claim("userId",userId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey())
                .compact();
    }
//              解析jwp
    private Claims parseClaims(String token){
        return  Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public Long getUserId(String token){
        return  parseClaims(token).get("userId",Long.class);
    }

    public String getUsername (String token){
        return  parseClaims(token).getSubject();
    }
//
// 验证 token 是否有效（是否过期、签名是否正确）
    public  boolean validateToken(String token){
        try {
            parseClaims(token);
            return true;
        }catch (Exception e){
            log.error("token无效，异常信息：{}",e.getMessage());
            return false;
        }
    }

}
