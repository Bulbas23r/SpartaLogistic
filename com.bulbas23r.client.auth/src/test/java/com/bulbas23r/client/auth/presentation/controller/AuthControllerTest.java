package com.bulbas23r.client.auth.presentation.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.bulbas23r.client.auth.application.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;


  @Test
  void login() throws Exception {
    LoginRequestDto dto = new LoginRequestDto("dlehdgk2","dlehdgkqla");
    mockMvc.perform(
        MockMvcRequestBuilders.post("/api/auth/login", dto)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );

  }

  @Test
  void refreshToken() {
  }

  @Test
  void logout() {
  }

  @Test
  void validateToken() {
  }
}