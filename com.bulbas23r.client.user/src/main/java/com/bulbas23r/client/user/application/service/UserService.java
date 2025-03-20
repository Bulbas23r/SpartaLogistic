package com.bulbas23r.client.user.application.service;

import com.bulbas23r.client.user.application.dto.UserDataForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserPatchRequestForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserResponseDto;
import com.bulbas23r.client.user.application.dto.UserResponseForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserSignUpRequestDto;
import com.bulbas23r.client.user.domain.model.User;
import common.utils.PageUtils;
import common.utils.PageUtils.CommonSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public interface UserService {

  UserResponseDto signUp(UserSignUpRequestDto userRequestDto);

  UserResponseDto getUserDetail(String username);

  UserResponseForRegisterDto getUserDetailForRegister(Long userId);

  UserResponseForRegisterDto<UserDataForRegisterDto> patchUser(Long userId, UserPatchRequestForRegisterDto patchDto);

  void deleteUser(Long userId);

  Page<User> getUsers(Pageable pageable);

  void updateUser(String username, UserPatchRequestForRegisterDto userRequestDto);

  Page<User> searchUser(Pageable pageable, Direction sortDirection, CommonSortBy sortBy, String keyword);
}
