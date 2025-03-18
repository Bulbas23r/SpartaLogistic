package com.bulbas23r.client.auth.infrastructure.config;

import com.bulbas23r.client.auth.application.dto.CustomUserDetails;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenProvider {
  // 하단의 방법 : 직접 Cookie 만들고 Cookie에 JWT 담아서 보냄. -> 장점 : Custom 용이, header에 Set-Cookie로 자동으로 넘어감 // 이 방법 외에는 헤더에 넣어서 보내는 방법이 있음 -> 장점 : 코드 수 감소 // 더 좋고 나쁘고는 없으니, 상황에 맞게 구현하면 됨.
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
  public String generateAccessToken(Authentication authentication) {
    Date date = new Date();
    Date expireDate = new Date(date.getTime() + ACCESS_TOKEN_TIME);

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    String role = userDetails.getAuthorities().stream()
        .findFirst()  // 단일 권한만 가져옴
        .map(GrantedAuthority::getAuthority)  // 권한 이름 추출
        .orElseThrow(() -> new IllegalArgumentException("사용자에게 권한이 없습니다."));

    //암호화
    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(authentication.getName()) // 사용자 식별자값 username
            .claim("id", userDetails.getId())
            .claim("role", role)
            .setIssuedAt(date) //발급일
            .setExpiration(expireDate) // 만료 시간
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  // JWT refreshToken 생성
  public String generateRefreshToken(Authentication authentication) {
    Date date = new Date();
    Date expireDate = new Date(date.getTime() + REFRESH_TOKEN_TIME);

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    String role = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("권한이 존재하지 않습니다."));

    //암호화
    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(authentication.getName()) // 사용자 식별자값
            .claim("id", userDetails.getId())
            .claim("role", role)
            .setIssuedAt(date) //발급일
            .setExpiration(expireDate) // 만료 시간
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  // JWT 토큰을 Substring ("Bearer " 제거)
  public String substringToken(String tokenValue) {
    if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
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
    } return false;
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInformationToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  //
  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  //JWT에서 username 추출
  public String getUsernameFromToken(String token) {
    Claims claims = getAllClaimsFromToken(token);
    return claims.getSubject();
  }

  //id 추출
  public Long getUserIdFromToken(String token) {
    Claims claims = getAllClaimsFromToken(token);
    return claims.get("id", Long.class);  // 사용자 ID 추출
  }

  //role
  public String getRolesFromToken(String token) {
    Claims claims = getAllClaimsFromToken(token);
    return claims.get("role", String.class);

  }
}
