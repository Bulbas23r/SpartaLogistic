package com.bulbas23r.client.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto<T> {
  private int resultCode;
  private String message;
  private UserDataDto data;
}
