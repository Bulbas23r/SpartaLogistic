package com.bulbas23r.client.message.domain.model;

import common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_slack_message")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Message extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String sender;
  private String receiver;
  private String message;
  private Date date;

  public Message(String sender, String receiver, String message, Date date) {
    this.sender = sender;
    this.receiver = receiver;
    this.message = message;
    this.date = date;
  }

  public void changeMessage(String newMessage) {
    this.message = newMessage;
  }
}
