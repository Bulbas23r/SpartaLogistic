package com.bulbas23r.client.auth.client;

import com.bulbas23r.client.auth.application.dto.UserDetailsDto;
import common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:19091/api/users", configuration = FeignConfig.class)
public interface UserClient {

  @GetMapping("/client/{username}")
  UserDetailsDto getUserDetails(@PathVariable String username);
}
