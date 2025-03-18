package common.aop;

import common.annotation.RoleCheck;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoleCheckAspect {
  @Before("@annotation(roleCheck)")
  public void checkRole(JoinPoint joinPoint, RoleCheck roleCheck) throws Throwable {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getAuthorities().stream().noneMatch(
        grantedAuthority -> grantedAuthority.getAuthority().equals(roleCheck.value())
    )) {
      throw new RuntimeException("Access Denied: Insufficient role");
    }
  }
}
