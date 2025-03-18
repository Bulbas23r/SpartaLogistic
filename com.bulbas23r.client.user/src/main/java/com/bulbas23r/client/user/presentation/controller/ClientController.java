package com.bulbas23r.client.user.presentation.controller;

import com.bulbas23r.client.user.application.service.ClientService;
import common.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

  private final ClientService clientService;

  @GetMapping("/login")
  public UserDetailsDto getUserDetails(@RequestParam String username) {
    return clientService.getUserDetails(username);
  }
}
