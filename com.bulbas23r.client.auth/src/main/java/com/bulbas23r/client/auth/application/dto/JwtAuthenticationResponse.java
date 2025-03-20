package com.bulbas23r.client.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
  private String accessToken;
  private String refreshToken;
  private Long userId;
  private String username;
  private String role;
}
