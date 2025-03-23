package com.bulbas23r.client.user.infrastructure.messaging;

import com.bulbas23r.client.user.domain.model.User;
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
    kafkaTemplate.send("user-delete-events", event);
  }
}
