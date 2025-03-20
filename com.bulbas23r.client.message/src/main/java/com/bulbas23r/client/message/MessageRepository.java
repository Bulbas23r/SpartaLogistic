package com.bulbas23r.client.message;

import com.bulbas23r.client.message.domain.Message;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

}
