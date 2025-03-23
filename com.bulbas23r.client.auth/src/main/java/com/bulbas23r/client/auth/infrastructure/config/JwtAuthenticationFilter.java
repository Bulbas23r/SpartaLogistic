package com.bulbas23r.client.auth.infrastructure.config;

import common.header.UserInfoHeader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            // 토큰에서 id, username, role 값을 추출 (토큰 파싱 로직은 JwtTokenProvider에 구현되어 있다고 가정)
            String id = tokenProvider.getUserIdFromToken(token);
            String username = tokenProvider.getUsernameFromToken(token);
            // 예시로 role이 여러 개일 경우 콤마로 연결된 문자열로 가정
            String role = tokenProvider.getRolesFromToken(token);

            // 헤더 추가를 위해 HttpServletRequest를 래핑(wrapping)
            HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if (UserInfoHeader.USER_ID.equalsIgnoreCase(name)) {
                        return id;
                    } else if (UserInfoHeader.USER_NAME.equalsIgnoreCase(name)) {
                        return username;
                    } else if (UserInfoHeader.USER_ROLE.equalsIgnoreCase(name)) {
                        return role;
                    }
                    return super.getHeader(name);
                }

                @Override
                public Enumeration<String> getHeaders(String name) {
                    if (UserInfoHeader.USER_ID.equalsIgnoreCase(name)) {
                        return Collections.enumeration(Collections.singletonList(id));
                    } else if (UserInfoHeader.USER_NAME.equalsIgnoreCase(name)) {
                        return Collections.enumeration(Collections.singletonList(username));
                    } else if (UserInfoHeader.USER_ROLE.equalsIgnoreCase(name)) {
                        return Collections.enumeration(Collections.singletonList(role));
                    }
                    return super.getHeaders(name);
                }
            };

            // Spring Cloud Gateway로 요청 전달 시 헤더에 id, username, role 정보를 포함시킴
            filterChain.doFilter(wrappedRequest, response);
            return;
        }

        // 토큰이 없거나 유효하지 않으면 기존 요청 그대로 진행
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 "Bearer " 토큰 부분만 추출
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 부분 추출
        }
        return null;
    }
}
