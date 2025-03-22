package com.bulbas23r.client.auth.infrastructure.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenProvider {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; //60min
    private final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L; //1일

    @Value("${jwt.key}")
    private String secretKey;
    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    //로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");


    @PostConstruct // 딱 한번만 호출하면 되는 자원에 씀. 또 호출하는 것 방지
    public void init() {
        byte[] bytes = secretKey.getBytes();
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JWT accessToken 생성
    public String generateAccessToken(String username, String role, String id) {
        Date date = new Date();
        Date expireDate = new Date(date.getTime() + ACCESS_TOKEN_TIME);

        //암호화
        return BEARER_PREFIX +
            Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(date) //발급일
                .setExpiration(expireDate) // 만료 시간
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    // JWT refreshToken 생성
    public String generateRefreshToken(String username, String role, String id) {
        Date date = new Date();
        Date expireDate = new Date(date.getTime() + REFRESH_TOKEN_TIME);

        return BEARER_PREFIX +
            Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(date) //발급일
                .setExpiration(expireDate) // 만료 시간
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    // JWT 토큰을 Substring ("Bearer " 제거)
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    //토큰 만료 여부 확인
    public boolean isTokenExpired(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            //토큰이 유효하지 않을 때
            return true;
        }
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT Signature, 유효하지 않은 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT Token, 만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInformationToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    //
    public Claims getAllClaimsFromToken(String tokenValue) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(tokenValue)
            .getBody();
    }

    //JWT에서 username 추출
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("username");
    }

    //id 추출
    public String getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("id", String.class);  // 사용자 ID 추출
    }

    //role
    public String getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }
}
