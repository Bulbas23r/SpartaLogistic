package com.bulbas23r.client.message.client;

import common.config.FeignConfig;
import common.dto.UserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:19091/api/users", configuration = FeignConfig.class)
public interface UserClient {
  @GetMapping("/client/slackId/{slackId}")
  String getUsername(@PathVariable("slackId") String slackId);

  @GetMapping("/info/{userId}")
  ResponseEntity<UserInfoResponseDto> getUser(@PathVariable("userId") Long userId);
}
