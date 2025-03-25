package com.bulbas23r.client.auth.application.service;

import com.bulbas23r.client.auth.application.dto.JwtAuthenticationResponse;
import com.bulbas23r.client.auth.application.dto.LoginRequestDto;
import com.bulbas23r.client.auth.application.dto.UserDetailsDto;
import com.bulbas23r.client.auth.client.UserClient;
import com.bulbas23r.client.auth.infrastructure.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final UserClient userClient;


    public JwtAuthenticationResponse login(LoginRequestDto loginDto) {
        UserDetailsDto userDetails = userClient.getUserDetails(loginDto.getUsername());
        // AccessToken, RefreshToken 생성
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails.getUsername(),
            String.valueOf(userDetails.getRole()), String.valueOf(userDetails.getUserId()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername(),
            String.valueOf(userDetails.getRole()), String.valueOf(userDetails.getUserId()));
        //사용자 정보

        // Redis에 RefreshToken 저장
        redisService.saveRefreshToken(loginDto.getUsername(), refreshToken);

        // 응답 DTO 반환
        return new JwtAuthenticationResponse(accessToken, refreshToken, userDetails.getUserId(),
            userDetails.getUsername(), String.valueOf(userDetails.getRole()));
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
                UserDetailsDto userDetails = userClient.getUserDetails(username);
                String newAccessToken = jwtTokenProvider.generateAccessToken(username,
                    String.valueOf(userDetails.getRole()), String.valueOf(userDetails.getUserId()));
                //return 새로운 accessToken과 기존 refreshToken
                return new JwtAuthenticationResponse(newAccessToken, refreshToken,
                    userDetails.getUserId(),
                    userDetails.getUsername(), String.valueOf(userDetails.getRole()));
            } else {
                //Todo : exception
                throw new IllegalArgumentException("Invalid refresh Token");
            }
        } else {
            throw new IllegalArgumentException("Refresh token is expired or invalid");
        }
    }

    public void logout(String username) {
        redisService.deleteRefreshToken(username);
    }
}
