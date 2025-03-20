package com.bulbas23r.client.user.presentation.controller;

import com.bulbas23r.client.user.application.dto.UserDataForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserPatchRequestForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserResponseDto;
import com.bulbas23r.client.user.application.dto.UserResponseForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserSignUpRequestDto;
import com.bulbas23r.client.user.application.service.ClientService;
import com.bulbas23r.client.user.application.service.UserService;
import com.bulbas23r.client.user.domain.model.User;
import common.annotation.RoleCheck;
import common.dto.UserDetailsDto;
import common.utils.PageUtils;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final ClientService clientService;

  @GetMapping("/client/{username}")
  public UserDetailsDto getUserDetails(@PathVariable String username) {
    return clientService.getUserDetails(username);
  }

  //회원가입
  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(
      @Valid @RequestBody UserSignUpRequestDto userRequestDto,
      BindingResult bindingResult
  ) throws Exception {
    if (bindingResult.hasErrors()) {
      String message = bindingResult.getAllErrors().stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .collect(Collectors.joining(", "));

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    UserResponseDto userResponseDto = userService.signUp(userRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
  }

  @PatchMapping
  public ResponseEntity<?> updateUser(
      @RequestHeader("X-User-Name") String username,
      @RequestBody UserPatchRequestForRegisterDto userRequestDto
  ) throws Exception {
    userService.updateUser(username, userRequestDto);
    return ResponseEntity.ok().build();
  }

  //유저 : 개인 상세 정보 조회
  @GetMapping()
  public ResponseEntity getUserDetail(@RequestHeader("X-User-Name") String username) {
    UserResponseDto userResponseDto = userService.getUserDetail(username);
    return ResponseEntity.ok(userResponseDto);
  }

  //관리자 : 유저 상세 정보 조회
  @RoleCheck("MASTER")
  @GetMapping("/{userId}")
  public ResponseEntity getUserDetailForRegister(@PathVariable("userId") Long userId) {
    UserResponseForRegisterDto userResponseForRegisterDto = userService.getUserDetailForRegister(
        userId);
    return ResponseEntity.ok(userResponseForRegisterDto);
  }

  //관리자 : 유저 리스트 조회
  @RoleCheck("MASTER")
  @GetMapping("/list")
  public ResponseEntity<Page<UserDataForRegisterDto>> getUsers(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size
  ) {
    Pageable pageable = PageUtils.pageable(page, size);
    Page<User> userList = userService.getUsers(pageable);
    return ResponseEntity.ok(userList.map(UserDataForRegisterDto::new));
  }

  //관리자 : 유저 검색
  @RoleCheck("MASTER")
  @GetMapping("/search")
  public ResponseEntity<?> searchUser(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size,
      @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
      @RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
      @RequestParam(required = false) String keyword) {
    Pageable pageable = PageUtils.pageable(page, size);
    Page<User> userList = userService.searchUser(pageable, sortDirection, sortBy, keyword);

    return ResponseEntity.ok(userList.map(UserDataForRegisterDto::new));
  }

  //관리자 : 유저 정보 수정
  @RoleCheck("MASTER")
  @PatchMapping("/{userId}")
  public ResponseEntity<?> patchUser(@PathVariable("userId") Long userId,
      @RequestBody UserPatchRequestForRegisterDto patchDto) {
    UserResponseForRegisterDto<UserDataForRegisterDto> patchUser =
        userService.patchUser(userId, patchDto);
    return ResponseEntity.ok(patchUser);
  }

  //관리자 : 유저 삭제
  @RoleCheck("MASTER")
  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok().build();
  }
}
