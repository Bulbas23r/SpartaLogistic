package common.aop;

import common.annotation.RoleCheck;
import common.exception.ForbiddenException;
import common.exception.NotFoundException;
import common.header.UserInfoHeader;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RoleCheckAspect {

    @Around("@annotation(roleCheck)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RoleCheck roleCheck) throws Throwable {
        // 어노테이션에 지정된 필수 역할 배열을 가져옴
        String[] requiredRoles = roleCheck.value();

        // 현재 HTTP 요청 정보를 가져옴
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new NotFoundException("요청 정보를 찾을 수 없습니다.");
        }
        HttpServletRequest request = attributes.getRequest();

        String headerRole = request.getHeader(UserInfoHeader.USER_ROLE);

        // 헤더에 역할이 없거나, 배열에 지정된 역할 중 하나와도 일치하지 않을 경우 예외 발생
        if (headerRole == null || Arrays.stream(requiredRoles)
            .noneMatch(role -> role.equals(headerRole))) {
            throw new ForbiddenException(
                "권한이 없습니다. (필요한 역할: " + String.join(", ", requiredRoles) + ")");
        }

        // 권한이 확인되면 대상 메소드 실행
        return joinPoint.proceed();
    }
}
