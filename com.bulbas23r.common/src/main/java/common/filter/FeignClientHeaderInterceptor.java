package common.filter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignClientHeaderInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate template) {
    // RequestContextHolder를 사용해 현재 HTTP 요청의 정보를 가져옵니다.
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null) {
        template.header("Authorization", authHeader);
        System.out.println("Feign Interceptor - Authorization header: " + authHeader);
      }
      // 필요 시 X-User-Name, X-Role 등 추가 헤더도 전달할 수 있습니다.
      String userName = request.getHeader("X-User-Name");
      if(userName != null) {
        template.header("X-User-Name", userName);
      }
      String role = request.getHeader("X-Role");
      if(role != null) {
        template.header("X-Role", role);
      }
    }
  }
}
