package com.bulbas23r.client.user.application.service;

import com.bulbas23r.client.user.domain.model.User;
import com.bulbas23r.client.user.domain.repository.UserRepository;
import common.dto.UserDetailsDto;
import common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ClientServiceImpl implements ClientService {
  private final UserRepository userRepository;

  public UserDetailsDto getUserDetails(String username) {
    //repository 조회
    try {
      User user = userRepository.findByUsername(username).orElseThrow();

      //to dto
      UserDetailsDto detailsDto = new UserDetailsDto();
      detailsDto.setUserId(user.getUserId());
      detailsDto.setUsername(user.getUsername());
      detailsDto.setPassword(user.getPassword());
      detailsDto.setRole(user.getUserRoleEnum());
      return detailsDto;
    } catch (Exception e ) {
      log.error(e.getMessage());
      return null;
    }
  }

  @Override
  public String getUserName(String slackId) {
    User user = userRepository.findBySlackId(slackId).orElseThrow(() -> new NotFoundException("해당 슬랙 아이디를 가진 유저가 존재하지 않습니다."));
    return user.getUsername();
  }
}
