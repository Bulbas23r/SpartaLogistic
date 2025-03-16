package com.bulbas23r.client.hub.hub.domain.repository;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {

    Hub save(Hub hub);

    Optional<Hub> findById(UUID hubId);
}
