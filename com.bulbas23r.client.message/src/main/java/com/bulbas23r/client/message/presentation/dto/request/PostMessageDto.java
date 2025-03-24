package com.bulbas23r.client.message.presentation.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class PostMessageDto {
  private String slackId;
  private String message;

  public PostMessageDto(String slackId, String message) {
    this.slackId = slackId;
    this.message = message;
  }
}
