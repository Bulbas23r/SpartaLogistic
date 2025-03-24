package com.bulbas23r.client.user.infrastructure.messaging;

import common.UserContextHolder;
import common.event.DeleteUserEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public UserEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendUserDeleteEvent(Long userId) {
    DeleteUserEventDto event = new DeleteUserEventDto(userId);
    event.setAuthorization(UserContextHolder.getAuthorization());
    event.setRole(UserContextHolder.getRole());
    event.setUsername(UserContextHolder.getUser());
    kafkaTemplate.send("user-delete-events", event);
  }
}
