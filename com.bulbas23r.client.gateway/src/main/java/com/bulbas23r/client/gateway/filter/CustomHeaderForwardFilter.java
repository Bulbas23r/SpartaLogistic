package com.bulbas23r.client.gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomHeaderForwardFilter implements GlobalFilter, Ordered {

  private final WebClient webClient;

  @Value("${auth.url}")
  private String AUTH_URL;

  public CustomHeaderForwardFilter(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange,
      GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    String requestPath = request.getURI().getPath();
    String authHeader = request.getHeaders().getFirst("Authorization");

    if (requestPath.startsWith("/api/auth") || requestPath.startsWith("/api/users/sign-up") || requestPath.startsWith("/api/users/client")) {
      return chain.filter(exchange);
    }

    // JWT가 존재하는 경우에만 검증 요청 실행
    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      return webClient.post()
          .uri(AUTH_URL + "/validate") // Use the full URL
          .header("Authorization", authHeader)
          .retrieve()
          .bodyToMono(AuthResponse.class)
          .flatMap(authResponse -> {
            // auth 서버가 성공적으로 인증 정보를 반환하면,
            // 다운스트림 요청에 사용자 정보 헤더 추가
            ServerHttpRequest modifiedRequest = request.mutate()
//                .header("X-User-Id",authResponse.getUserId())
                .header("Authorization", authHeader)
                .header("X-User-Name", authResponse.getUsername())
                .header("X-Role", authResponse.getRole())
                .build();

            log.info("authResponse.getUsername : {} ", authResponse.getUsername());

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
          })
          .onErrorResume(ex -> {
            // Log the error for debugging
            System.err.println("Authentication error: " + ex.getMessage());
            // 인증 실패 시 401 Unauthorized 응답
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
          });
    } else {
      // If no Authorization header is present, return 401
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
