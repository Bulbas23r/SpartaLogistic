package common.filter;

import common.UserContextHolder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserContextFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      HttpServletRequest httpRequest = (HttpServletRequest) request;

      // 디버깅을 위한 전체 헤더 로그 출력
      Enumeration<String> headerNames = httpRequest.getHeaderNames();
      if (headerNames != null) {
        while (headerNames.hasMoreElements()) {
          String headerName = headerNames.nextElement();
          logger.debug("Header {} : {}", headerName, httpRequest.getHeader(headerName));
        }
      }

      // "X-User-Name" 헤더 값 가져오기
      String username = httpRequest.getHeader("X-User-Name");
      logger.info("X-User-Name header: {}", username);

      if (username != null && !username.trim().isEmpty()) {
        UserContextHolder.setCurrentUser(username);
      } else {
        logger.warn("X-User-Name header is missing or empty");
      }

      chain.doFilter(request, response);
    } finally {
      // 요청 처리 후 ThreadLocal 정리
      UserContextHolder.clear();
    }
  }
}
