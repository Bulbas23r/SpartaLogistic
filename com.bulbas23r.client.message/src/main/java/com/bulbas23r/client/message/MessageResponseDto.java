package com.bulbas23r.client.message;

import com.bulbas23r.client.message.domain.Message;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponseDto {
  private UUID id;
  private String receiver;
  private String sender;
  private String message;
  private Date sendTime;

  public MessageResponseDto(Message message) {
    this.id = message.getId();
    this.receiver = message.getReceiver();
    this.sender = message.getSender();
    this.message = message.getMessage();
    this.sendTime = message.getDate();
  }
}
