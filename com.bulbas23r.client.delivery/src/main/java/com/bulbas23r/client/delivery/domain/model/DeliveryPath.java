package com.bulbas23r.client.delivery.domain.model;

import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_delivery_path")
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPath extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(name = "hub_transit_info_id",nullable = false)
    private UUID hubTransitInfoId;

    @Column(name = "start_hub_id",nullable = false)
    private UUID startHubId;

    @Column(name = "end_hub_id",nullable = false)
    private UUID endHubId;

    @Column(name = "delivery_manager_id",nullable = false)
    private UUID deliveryManagerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryPathStatus status;

    @Column(nullable = false)
    private Integer sequence;

    @Column(name = "estimated_distance")
    private Double estimatedDistance;

    @Column(name = "estimated_duration")
    private Duration estimatedDuration;

    @Column(name = "actual_distance")
    private Double actualDistance;

    @Column(name = "actual_duration")
    private Duration actualDuration;
}
