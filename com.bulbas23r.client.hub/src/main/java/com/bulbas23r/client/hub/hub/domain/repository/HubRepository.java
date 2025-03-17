package com.bulbas23r.client.hub.hub.domain.repository;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRepository {

    Hub save(Hub hub);

    Optional<Hub> findById(UUID hubId);

    Page<Hub> findAll(Pageable pageable);
}
