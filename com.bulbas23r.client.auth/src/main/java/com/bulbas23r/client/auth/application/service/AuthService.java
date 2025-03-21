package com.bulbas23r.client.auth.application.service;

import com.bulbas23r.client.auth.application.dto.JwtAuthenticationResponse;
import com.bulbas23r.client.auth.application.dto.LoginRequestDto;

public interface AuthService {

  void logout(String username);

  JwtAuthenticationResponse refreshToken(String refreshToken);

  JwtAuthenticationResponse login(LoginRequestDto loginDto);
}
