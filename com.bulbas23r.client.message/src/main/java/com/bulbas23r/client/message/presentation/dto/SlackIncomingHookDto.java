package com.bulbas23r.client.message.presentation.dto;

import lombok.Data;

@Data
public class SlackIncomingHookDto {
  private String text;  // Slack으로 전송될 메시지 내용

  /**
   * 예) new SlackIncomingHookDto("@U1234567", "테스트 메시지")
   * => 최종적으로 {"text":"@U1234567 테스트 메시지"} 형태로 전송됨
   */
  public SlackIncomingHookDto(String sender, String message) {
    this.text = "송신자 : " + sender  + "\n내용 : " + message;
  }
}
