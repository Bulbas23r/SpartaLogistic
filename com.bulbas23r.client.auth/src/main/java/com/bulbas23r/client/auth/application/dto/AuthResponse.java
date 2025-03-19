package com.bulbas23r.client.auth.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
  private String userId;
  private String username;
  private String role;
}
