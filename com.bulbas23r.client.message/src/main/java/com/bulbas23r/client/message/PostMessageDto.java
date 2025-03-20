package com.bulbas23r.client.message;

import lombok.Data;

@Data
public class PostMessageDto {
  private String slackId;
  private String message;
}
