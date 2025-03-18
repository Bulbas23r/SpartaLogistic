package com.bulbas23r.client.user.application.service;

import com.bulbas23r.client.user.application.dto.UserDataDto;
import com.bulbas23r.client.user.application.dto.UserDataForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserPatchRequestForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserResponseDto;
import com.bulbas23r.client.user.application.dto.UserResponseForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserSignUpRequestDto;
import com.bulbas23r.client.user.domain.model.User;
import com.bulbas23r.client.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  //회원가입
  public UserResponseDto signUp(UserSignUpRequestDto signUpRequestDto) {
    // username 중복 확인 Todo : Exception
    userRepository.findByUsername(signUpRequestDto.getUsername());
    // Todo : password 인코딩 (객체 생성 전)
    String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
    // Todo : entity 변환
    User user = new User(signUpRequestDto, encodedPassword);
    // db 저장
    userRepository.save(user);
    // dto 변환
    UserDataDto userDataDto = new UserDataDto(user);
    // ResponseDto
    return new UserResponseDto<UserDataDto>(201, "회원가입 성공", userDataDto);
  }

  //관리자 : 유저 가입 승인
  public UserResponseForRegisterDto approve(Long userId, boolean isConfirmed) {
    //repository 찾기
    User user = userRepository.findById(userId).orElseThrow();
    // true로 변경
    user.setConfirmed(isConfirmed);
    //User 승인 여부 db 저장
    userRepository.save(user);
    //dto로 변환
    UserDataForRegisterDto userDataForRegisterDto = new UserDataForRegisterDto(user);
    return new UserResponseForRegisterDto<UserDataForRegisterDto>(200, "유저 생성 승인 성공", userDataForRegisterDto);
  }

  // 유저 : 개인의 상세 정보 조회
  public UserResponseDto getUserDetail(Long userId) {
    // repository 찾기
    User user = userRepository.findById(userId).orElseThrow();
    // dto 변환
    UserDataDto userDataDto = new UserDataDto(user);
    return new UserResponseDto<UserDataDto>(200, "유저의 개인 상세 정보 조회 성공", userDataDto);
  }

  // 관리자 : 유저의 상세 정보 조회
  public UserResponseForRegisterDto getUserDetailForRegister(Long userId) {
    //repository 찾기
    User user = userRepository.findById(userId).orElseThrow();
    //dto 변환
    UserDataForRegisterDto userDataForRegisterDto = new UserDataForRegisterDto(user);
    return new UserResponseForRegisterDto<UserDataForRegisterDto>(200, "유저 상세 정보 조회 성공", userDataForRegisterDto);
  }

//  // 관리자 : 유저 리스트 조회
//  public CustomPageResponse<UserDataForRegisterDto> getUsers(int page, int size, String sort) {
//    //sort
//    Sort sortBy = Sort.by(Sort.Direction.DESC, sort);
//    Pageable pageable = PageRequest.of(page, size, sortBy);
//    // repository 검색
//    Page<User> userList = userRepository.findAll(pageable);
//    // Dto 변환
//    Page<UserDataForRegisterDto> dtoPage = userList.map(UserDataForRegisterDto:: new);
//    return new CustomPageResponse<>(dtoPage);
//  }
//
//  // 관리자 : 유저 검색
//  public CustomPageResponse<UserDataForRegisterDto> searchUsers(
//      String searchText, int page, int size, String sort) {
//    //sort
//    Sort sortBy = Sort.by(Sort.Direction.DESC, sort);
//    Pageable pageable = PageRequest.of(page, size, sortBy);
//
//    Page<User> searchUsers = userRepository.search(searchText, pageable);
//    // to Dto
//    Page<UserDataForRegisterDto> userList = searchUsers.map(UserDataForRegisterDto::new);
//    return new CustomPageResponse<>(userList);
//
//  }
//
  //관리자 : 유저 수정
  public UserResponseForRegisterDto patchUser(Long userId, UserPatchRequestForRegisterDto patchDto) {
    //repository 검색
    User user = userRepository.findById(userId).orElseThrow();
    //수정 및 유효성 검사(unique 값)
    if(patchDto.getName() == null || patchDto.getUsername() == null ||
        patchDto.getSlackId() == null || patchDto.getUserRoleEnum() == null) {
      throw new IllegalArgumentException("");
    }
    user.setUsername(patchDto.getName());
    user.setUsername(patchDto.getUsername());
    user.setSlackId(patchDto.getSlackId());
    user.setUserRoleEnum(patchDto.getUserRoleEnum());
    user.setConfirmed(patchDto.isConfirmed());
    //db 저장
    userRepository.save(user);
    //to dto
    UserDataForRegisterDto patchedUserDto = new UserDataForRegisterDto(user);
    //return
    return new UserResponseForRegisterDto<>(200, "유저 정보 수정 완료", patchedUserDto);
  }

  //관리자 : 유저 삭제
  public void deleteUser(Long userId) {
    //repository 찾기
    User user = userRepository.findById(userId).orElseThrow();
    //논리적 삭제 -> isDeleted = true
    user.setDeleted(true);
    //db 저장
    userRepository.save(user);
  }
}
