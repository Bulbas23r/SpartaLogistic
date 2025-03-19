package com.bulbas23r.client.user.application.dto;

import common.model.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {

  @NotBlank(message = "아이디는 필수 입력값입니다.")
  @Size(min = 4, max = 10, message = "아이디는 4자 이상, 10자 이하만 가능합니다.")
  @Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 알파벳 소문자와 숫자로만 구성되어야 합니다.")
  private String username;
  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  @Size(min = 8, max = 15, message = "비밀번호는 8자 이상, 15자 이하만 가능합니다.")
  @Pattern(
      regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$",
      message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자로만 구성되어야 합니다."
  )
  private String password;
  private String slackId;
  private String name;
  private UserRoleEnum userRoleEnum;
}
