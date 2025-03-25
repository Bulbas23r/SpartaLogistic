package com.bulbas23r.client.user.application.service;

import com.bulbas23r.client.user.application.dto.UserDataDto;
import com.bulbas23r.client.user.application.dto.UserDataForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserPatchRequestForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserResponseDto;
import com.bulbas23r.client.user.application.dto.UserResponseForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserSignUpRequestDto;
import com.bulbas23r.client.user.domain.model.User;
import com.bulbas23r.client.user.domain.repository.UserRepository;
import com.bulbas23r.client.user.domain.repository.UserRepositoryCustom;
import common.exception.BadRequestException;
import common.exception.NotFoundException;
import common.utils.PageUtils.CommonSortBy;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public UserResponseDto signUp(UserSignUpRequestDto signUpRequestDto) {
        Optional<User> findUser = userRepository.findByUsername(signUpRequestDto.getUsername());

        if (findUser.isPresent()) {
            throw new BadRequestException("이미 존재하는 아이디 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        User user = new User(signUpRequestDto, encodedPassword);
        // db 저장
        userRepository.save(user);
        // dto 변환
        UserDataDto userDataDto = new UserDataDto(user);
        // ResponseDto
        return new UserResponseDto<UserDataDto>(201, "회원가입 성공", userDataDto);
    }

    @Override
    public UserResponseDto getUserDetail(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        //dto 변환
        UserDataDto userDataDto = new UserDataDto(user);
        return new UserResponseDto(200, "유저 상세 정보 조회 성공", userDataDto);
    }

    @Override
    public UserResponseForRegisterDto getUserDetailForRegister(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        UserDataForRegisterDto userDataForRegisterDto = new UserDataForRegisterDto(user);
        return new UserResponseForRegisterDto<UserDataForRegisterDto>(200, "유저 상세 정보 조회 성공",
            userDataForRegisterDto);
    }

    // 유저 : 개인의 상세 정보 조회
    public UserResponseDto getUserDetail(Long userId) {
        // repository 찾기
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        // dto 변환
        UserDataDto userDataDto = new UserDataDto(user);
        return new UserResponseDto<UserDataDto>(200, "유저의 개인 상세 정보 조회 성공", userDataDto);
    }

    //관리자 : 유저 수정
    public UserResponseForRegisterDto patchUser(Long userId,
        UserPatchRequestForRegisterDto patchDto) {
        //repository 검색
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        //수정 및 유효성 검사(unique 값)
        if (patchDto.getName() == null || patchDto.getUsername() == null ||
            patchDto.getSlackId() == null || patchDto.getUserRoleEnum() == null) {
            throw new IllegalArgumentException("");
        }
        user.setUsername(patchDto.getName());
        user.setUsername(patchDto.getUsername());
        user.setSlackId(patchDto.getSlackId());
        user.setUserRoleEnum(patchDto.getUserRoleEnum());
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
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        //논리적 삭제 -> isDeleted = true
        user.setDeleted();
        //db 저장
        userRepository.save(user);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void updateUser(String username, UserPatchRequestForRegisterDto userRequestDto) {
        userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
        User user = new User(userRequestDto);
    }

    @Override
    public Page<User> searchUser(Pageable pageable, Direction sortDirection, CommonSortBy sortBy,
        String keyword) {
        return userRepositoryCustom.searchUser(pageable, sortDirection, sortBy, keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException("존재하지 않는 유저입니다!")
        );
    }
}
