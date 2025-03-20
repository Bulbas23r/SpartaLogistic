package com.bulbas23r.client.gateway.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
  private String userId;
  private String username;
  private String role;
}
