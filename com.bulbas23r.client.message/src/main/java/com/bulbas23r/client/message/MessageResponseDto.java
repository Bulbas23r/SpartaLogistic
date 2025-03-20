package com.bulbas23r.client.message;

import static org.bouncycastle.asn1.x500.style.BCStyle.T;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MessageResponseDto {
  private String receiverId;
  private String senderId;
  private String message;
  private LocalDateTime sendTime;

}
