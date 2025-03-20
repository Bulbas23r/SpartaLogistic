package com.bulbas23r.client.message.domain;

import common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "p_slack_message")
public class Message extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String senderId;
  private String receiverId;
  private String message;
  private Date date;
}
