package com.bulbas23r.client.hub.route.domain.model;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import com.bulbas23r.client.hub.route.presentation.dto.CreateRouteRequestDto;
import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted is false")
@Table(name = "p_route")
public class Route extends BaseEntity {

    @EmbeddedId
    private RouteId id;

    @Column(nullable = false)
    private Integer transitTime;

    @Column(nullable = false)
    private Integer transitDistance;

    @Column(columnDefinition = "boolean default false")
    private boolean active;

    public Route(Hub departureHub, Hub arrivalHub, Integer transitTime, Integer transitDistance) {
        this.id = new RouteId(departureHub.getId(), arrivalHub.getId());
        this.transitTime = transitTime;
        this.transitDistance = transitDistance;
        active = true;
    }

    public Route(CreateRouteRequestDto requestDto) {
        this.id = new RouteId(requestDto.getDepartureHubId(), requestDto.getArrivalHubId());
        this.transitTime = requestDto.getTransitTime();
        this.transitDistance = requestDto.getTransitDistance();
    }
}
