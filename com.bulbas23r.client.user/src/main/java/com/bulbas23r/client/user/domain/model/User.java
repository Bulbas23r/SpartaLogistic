package com.bulbas23r.client.user.domain.model;

import com.bulbas23r.client.user.application.dto.UserPatchRequestForRegisterDto;
import com.bulbas23r.client.user.application.dto.UserSignUpRequestDto;
import common.dto.UserInfoResponseDto;
import common.model.BaseEntity;
import common.model.UserRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_users", schema = "USER_SCHEMA")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) //enumType 이름 그대로 db저장
    private UserRoleEnum userRoleEnum;

    public User(UserSignUpRequestDto signUpRequestDto, String password) {
        this.username = signUpRequestDto.getUsername();
        this.password = password;
        this.slackId = signUpRequestDto.getSlackId();
        this.name = signUpRequestDto.getName();
        this.userRoleEnum = signUpRequestDto.getUserRoleEnum();
    }


    public User(UserPatchRequestForRegisterDto userRequestDto) {
        this.username = userRequestDto.getUsername();
        this.slackId = userRequestDto.getSlackId();
        this.name = userRequestDto.getName();
        this.userRoleEnum = userRequestDto.getUserRoleEnum();
    }

    public UserInfoResponseDto toUserInfoResponseDto() {
        return UserInfoResponseDto.builder()
            .userId(this.userId)
            .username(this.username)
            .role(this.userRoleEnum)
            .build();
    }
}
