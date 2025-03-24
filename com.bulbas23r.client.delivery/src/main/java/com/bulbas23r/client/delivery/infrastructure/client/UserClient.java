package com.bulbas23r.client.delivery.infrastructure.client;

import common.config.FeignConfig;
import common.dto.UserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:19092/api/users")
public interface UserClient {

    @GetMapping("/info/{userId}")
    ResponseEntity<UserInfoResponseDto> getUserInfo(@PathVariable("userId") Long userId);
}
