package com.bulbas23r.client.deliverymanager.infrastructure.aspect;

import com.bulbas23r.client.deliverymanager.application.service.DeliveryManagerService;
import common.header.UserInfoHeader;
import common.model.UserRoleEnum.Authority;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class HubManagerCheckAspect {

    private final DeliveryManagerService deliveryManagerService;

    @Pointcut("@annotation(com.bulbas23r.client.deliverymanager.infrastructure.annotation.HubManagerCheck)")
    private void hubManagerCheck() {
    }

    @Before("hubManagerCheck()")
    public void checkOrderOwner(JoinPoint joinPoint) {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) {
            throw new IllegalStateException("Request not found");
        }

        String userId = request.getHeader(UserInfoHeader.USER_ID);
        String userRole = request.getHeader(UserInfoHeader.USER_ROLE);

        if (userId == null || userRole == null) {
            throw new IllegalArgumentException(
                "Missing required headers: X-USER_ID or X-USER_ROLE");
        }

        if (userRole.equals(Authority.HUB_MANAGER)) {
            System.out.println("?????????? 호출됨 ????????????/ㅓㅏ");
//            deliveryManagerService.checkHubManager(Long.parseLong(userId));
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
