package com.bulbas23r.client.user.application.service;

import common.dto.UserDetailsDto;

public interface ClientService {

  UserDetailsDto getUserDetails(String username);

  String getUserName(String slackId);
}
