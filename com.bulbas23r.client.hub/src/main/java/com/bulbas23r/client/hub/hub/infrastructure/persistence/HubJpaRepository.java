package com.bulbas23r.client.hub.hub.infrastructure.persistence;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.hub.domain.repository.HubRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubJpaRepository extends HubRepository, JpaRepository<Hub, UUID> {

}
