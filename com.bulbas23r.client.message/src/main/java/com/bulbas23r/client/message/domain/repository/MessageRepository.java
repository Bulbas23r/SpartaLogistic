package com.bulbas23r.client.message.domain.repository;

import com.bulbas23r.client.message.domain.model.Message;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepository {

  Message save(Message message);

  Page<Message> findAll(Pageable pageable);

  Optional<Message> findById(UUID messageId);
}
