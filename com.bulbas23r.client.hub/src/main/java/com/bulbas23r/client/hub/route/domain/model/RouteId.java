package com.bulbas23r.client.hub.route.domain.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteId implements Serializable {

    private UUID departureHubId;
    private UUID arrivalHubId;

    public RouteId(UUID departureHubId, UUID arrivalHubId) {
        if (departureHubId == null || arrivalHubId == null) {
            throw new IllegalArgumentException("경로 ID는 필수입니다.");
        }
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
    }
}
