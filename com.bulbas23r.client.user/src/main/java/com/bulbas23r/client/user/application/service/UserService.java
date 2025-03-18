package com.bulbas23r.client.user.application.service;

import com.bulbas23r.client.user.application.dto.UserDataForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserPatchRequestForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserResponseDto;
import com.bulbas23r.client.user.application.dto.UserResponseForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserSignUpRequestDto;

public interface UserService {


  UserResponseDto signUp(UserSignUpRequestDto userRequestDto);

  UserResponseForRegisterDto approve(Long userId, boolean isConfirmed);

  UserResponseDto getUserDetail(Long userId);

  UserResponseForRegisterDto getUserDetailForRegister(Long userId);

  UserResponseForRegisterDto<UserDataForRegisterDto> patchUser(Long userId, UserPatchRequestForRegisterDto patchDto);

  void deleteUser(Long userId);
}
