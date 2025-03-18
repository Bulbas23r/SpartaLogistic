package com.bulbas23r.client.user.application.dto;

import common.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchRequestForRegisterDto {
  private String username;
  private String name;
  private String slackId;
  private UserRoleEnum userRoleEnum;
  private boolean isConfirmed;
}
