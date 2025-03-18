package com.bulbas23r.client.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtFilter implements ServerSecurityContextRepository {

  @Value("${jwt.key}")
  private String secretKey;

  //username 추출
  private String getUsername(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getSubject();
  }

  //권한 추출
  private String getRole(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.get("role", String.class);
  }

  //id 추출
  private String getUserId(String token) {
    Claims claims = getClaimsFromToken(token);
    Integer id = claims.get("id", Integer.class);
    return id.toString();
  }

  //Claims 추출
  private Claims getClaimsFromToken(String token) {
    byte[] bytes = secretKey.getBytes();
    SecretKey key = Keys.hmacShaKeyFor(bytes);
    Jws<Claims> claimsJws = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);
    return claimsJws.getBody();
  }

  // 토큰 헤더에서 추출
  private String extractToken(ServerWebExchange exchange) {
    ServerHttpRequest request = exchange.getRequest();
    String authHeader = request.getHeaders().getFirst("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  //토큰 검증
  private boolean validateToken(String token, ServerWebExchange exchange) {
    try {
      byte[] bytes = secretKey.getBytes();
      SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    return Mono.empty();
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    log.info("path : {}", exchange.getRequest().getPath());
    String path = exchange.getRequest().getURI().getPath();
    if (path.equals("/api/auth/login") || path.equals("/api/users/sign-up") || path.equals("/api/auth/refresh-token")) {
      return Mono.empty();
    }

    String token = extractToken(exchange);

    if (token == null || !validateToken(token, exchange)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      log.info(exchange.getRequest().getURI().getPath());
      return Mono.empty();
    }
    String role = getRole(token);
    String userId = getUserId(token);  // ID 추출 메서드
    String username = getUsername(token);  // 사용자명 추출 메서드
    //username, null, role
    Collection<GrantedAuthority> roleCollection = List.of(() -> role);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, roleCollection);
    exchange.mutate()
        .request(r -> r.headers(headers -> {
          headers.add("X-User-Id", userId);
          headers.add("X-User-Name", username);
          headers.add("X-User-Role", role);
        }))
        .build();

    return Mono.just(new SecurityContextImpl(authentication));  // SecurityContext 생성
  }
}
