package com.bulbas23r.client.message.presentation.dto.response;

import lombok.Data;

@Data
public class GeminiAnswerDto {
  private String answer;

  public GeminiAnswerDto(String answer) {
    this.answer = answer;
  }
}
