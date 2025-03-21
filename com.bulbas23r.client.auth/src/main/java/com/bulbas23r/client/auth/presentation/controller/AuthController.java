package com.bulbas23r.client.auth.presentation.controller;

import com.bulbas23r.client.auth.application.dto.AuthResponse;
import com.bulbas23r.client.auth.application.dto.JwtAuthenticationResponse;
import com.bulbas23r.client.auth.application.dto.LoginRequestDto;
import com.bulbas23r.client.auth.application.dto.LoginResponseDto;
import com.bulbas23r.client.auth.application.dto.RefreshTokenRequest;
import com.bulbas23r.client.auth.application.dto.UserDetailsDto;
import com.bulbas23r.client.auth.application.service.AuthService;
import com.bulbas23r.client.auth.client.UserClient;
import com.bulbas23r.client.auth.infrastructure.config.JwtTokenProvider;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserClient userClient;

  //login
  @PostMapping("/login")
  public ResponseEntity login(
      @Valid @RequestBody LoginRequestDto loginDto,
      BindingResult bindingResult
  ) {

    if (bindingResult.hasErrors()) {
      String message = bindingResult.getAllErrors().stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .collect(Collectors.joining(", "));

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    //header 설정
    JwtAuthenticationResponse response = authService.login(loginDto);
    LoginResponseDto loginResponseDto = new LoginResponseDto(response.getAccessToken(),
        response.getRefreshToken());

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-User-Name", response.getUsername());
    headers.set("X-User-Role", response.getRole());

    return ResponseEntity.ok().headers(headers).body(loginResponseDto);
  }

  //refreshToken refresh
  @PostMapping("/refresh-token")
  public ResponseEntity refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    String accessToken = refreshTokenRequest.getAccessToken();
    String refreshToken = refreshTokenRequest.getRefreshToken();

    //accessToken 만료 검증
    boolean isExpired = jwtTokenProvider.isTokenExpired(accessToken);
    if (isExpired) {
      JwtAuthenticationResponse response = authService.refreshToken(refreshToken);
      LoginResponseDto loginResponseDto = new LoginResponseDto(
          response.getAccessToken().substring(7), response.getRefreshToken());

      HttpHeaders headers = new HttpHeaders();
      headers.set("X-User-Name", response.getUsername());
      headers.set("X-User-Role", response.getRole());

      return ResponseEntity.ok().headers(headers).body(loginResponseDto);
    } else {
      return ResponseEntity.ok("Token is still valid");
    }
  }

  //로그아웃
  @PostMapping("/logout")
  public ResponseEntity logout(@RequestHeader("X-User-Name") String username) {
    authService.logout(username);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/validate")
  public ResponseEntity<AuthResponse> validateToken(
      @RequestHeader("Authorization") String authHeader) {
    // "Bearer " 제거
    String token = authHeader.substring(7);
    if (!jwtTokenProvider.validateToken(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    AuthResponse response = new AuthResponse();
    response.setUsername(jwtTokenProvider.getUsernameFromToken(token));
    response.setRole(jwtTokenProvider.getRolesFromToken(token));

    return ResponseEntity.ok(response);
  }
}
