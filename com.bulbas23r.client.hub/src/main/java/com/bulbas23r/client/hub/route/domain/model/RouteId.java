package com.bulbas23r.client.hub.route.domain.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteId implements Serializable {

    private String departureHubId;
    private String arrivalHubId;

    public RouteId(String departureHubId, String arrivalHubId) {
        if (departureHubId == null || arrivalHubId == null) {
            throw new IllegalArgumentException("경로 ID는 필수입니다.");
        }
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
    }
}
