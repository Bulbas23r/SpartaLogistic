package com.bulbas23r.client.delivery.domain.model;

import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@Table(name = "p_delivery_route")
@SQLRestriction("is_deleted IS FALSE")
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRoute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;


    @Column(name = "departure_hub_id",nullable = false)
    private UUID departureHubId;

    @Column(name = "arrival_hub_id",nullable = false)
    private UUID arrivalHubId;

    @Column(name = "delivery_manager_id")
    private UUID deliveryManagerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryRouteStatus status;

    @Column(nullable = false)
    private Integer sequence;

    @Column(name = "estimated_distance")
    private Integer estimatedDistance;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Column(name = "actual_distance")
    private Integer actualDistance;

    @Column(name = "actual_duration")
    private Integer actualDuration;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = DeliveryRouteStatus.HUB_PENDING;  // 기본값 설정
        }
    }

    public void updateDepartDelivery(UUID deliveryManagerId) {
        this.status = DeliveryRouteStatus.HUB_TRANSIT;
        this.deliveryManagerId = deliveryManagerId;
    }


    public void updateArrivalDelivery(Integer actualDistance, Integer actualDuration) {
        this.status = DeliveryRouteStatus.HUB_ARRIVED;
        this.actualDistance = actualDistance;
        this.actualDuration = actualDuration;

    }
}
