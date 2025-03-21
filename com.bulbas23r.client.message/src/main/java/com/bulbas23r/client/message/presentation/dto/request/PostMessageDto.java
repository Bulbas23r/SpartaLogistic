package com.bulbas23r.client.message.presentation.dto.request;

import lombok.Data;

@Data
public class PostMessageDto {
  private String slackId;
  private String message;
}
