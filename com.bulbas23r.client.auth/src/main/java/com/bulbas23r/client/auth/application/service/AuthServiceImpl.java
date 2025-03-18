package com.bulbas23r.client.auth.application.service;

import com.bulbas23r.client.auth.application.dto.CustomUserDetails;
import com.bulbas23r.client.auth.application.dto.JwtAuthenticationResponse;
import com.bulbas23r.client.auth.application.dto.LoginRequestDto;
import com.bulbas23r.client.auth.application.dto.LoginResponseDto;
import com.bulbas23r.client.auth.infrastructure.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisService redisService;
  private final CustomUserDetailsService customUserDetailsService;


  public JwtAuthenticationResponse login(LoginRequestDto loginDto) throws AuthenticationException {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginDto.getUsername(),
            loginDto.getPassword()
        )
    );

    // AccessToken, RefreshToken 생성
    String accessToken = jwtTokenProvider.generateAccessToken(authentication);
    String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
    //사용자 정보
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    Long userId = userDetails.getId();  // Long 타입의 ID
    String username = userDetails.getUsername();  // 사용자 이름
    String role = userDetails.getAuthorities().iterator().next().getAuthority();


    // Redis에 RefreshToken 저장
    redisService.saveRefreshToken(authentication.getName(), refreshToken);

    // 응답 DTO 반환
    return new JwtAuthenticationResponse(accessToken, refreshToken, userId, username, role);
  }

  //refreshToken
  public JwtAuthenticationResponse refreshToken(String refreshToken) {
    //refreshToken 유효성 확인
    if (jwtTokenProvider.validateToken(refreshToken)) {
      String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
      //Redis에서 refreshToken 확인
      String storedToken = redisService.getRefreshToken(username).substring(7);
      if (storedToken != null && storedToken.equals(refreshToken)) {
        //new accessToken
        // CustomUserDetailsService를 이용해 사용자 정보 가져오기
        CustomUserDetails userDetail = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        //userDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String userDetailsUsername = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        //return 새로운 accessToken과 기존 refreshToken
        return new JwtAuthenticationResponse(newAccessToken, refreshToken, userId, userDetailsUsername, role);
      } else {
        //Todo : exception
        throw new IllegalArgumentException("Invalid refresh Token");
      }
    }else {
      throw new IllegalArgumentException("Refresh token is expired or invalid");
    }
  }

  public void logout(String username) {
    redisService.deleteRefreshToken(username);
  }
}
