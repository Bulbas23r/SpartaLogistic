package com.bulbas23r.client.message.infrastructure.persistence;

import com.bulbas23r.client.message.domain.model.Message;
import com.bulbas23r.client.message.domain.repository.MessageRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMessageRepository extends MessageRepository, JpaRepository<Message, UUID> {

}
