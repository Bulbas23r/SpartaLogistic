package com.bulbas23r.client.delivery.domain.model;

import ch.qos.logback.core.util.StringUtil;
import com.bulbas23r.client.delivery.application.dto.DeliveryUpdateRequestDto;
import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@Entity
@Table(name = "p_delivery")
@SQLRestriction("is_deleted IS FALSE")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id",nullable = false)
    private UUID orderId;

    @Column(name = "departure_hub_id", nullable = false)
    private UUID departureHubId;

    @Column(name = "arrival_hub_id", nullable = false)
    private UUID arrivalHubId;

    @Column(name = "delivery_manager_id", nullable = false)
    private UUID deliveryManagerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(name = "receiver_company_id",nullable = false)
    private UUID receiverCompanyId;

    @Column(name = "receiver_company_slack_id",nullable = false)
    private String receiverCompanySlackId;

    public void update(DeliveryUpdateRequestDto requestDto) {
        if(requestDto.getDepartureHubId() != null) this.departureHubId = requestDto.getDepartureHubId();
        if(requestDto.getArrivalHubIdHubId() != null) this.arrivalHubId = requestDto.getArrivalHubIdHubId();
        if(requestDto.getDeliveryManagerId() != null) this.deliveryManagerId = requestDto.getDeliveryManagerId();
        if(requestDto.getReceiverCompanyId() != null) this.receiverCompanyId = requestDto.getReceiverCompanyId();
        if(StringUtil.isNullOrEmpty(requestDto.getReceiverCompanySlackId())) this.receiverCompanySlackId = requestDto.getReceiverCompanySlackId();
    }


    public void changeStatus(DeliveryStatus status) {
        this.status = status;
    }

}
