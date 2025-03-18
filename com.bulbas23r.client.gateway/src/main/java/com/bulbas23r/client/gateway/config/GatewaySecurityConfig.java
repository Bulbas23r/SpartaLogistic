package com.bulbas23r.client.gateway.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.bulbas23r.client.gateway.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class GatewaySecurityConfig {

  private final JwtFilter jwtFilter;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http.csrf(CsrfSpec::disable)  // CSRF 비활성화 (필요 시 활성화)
        .authorizeExchange(exchange -> exchange
            .pathMatchers("/api/auth/**", "/api/users/sign-up").permitAll()
            .anyExchange().authenticated()
        )
        .securityContextRepository(jwtFilter);

    return http.build();
  }
}
