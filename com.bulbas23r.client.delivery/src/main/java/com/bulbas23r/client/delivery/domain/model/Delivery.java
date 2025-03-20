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

    @Column(name = "start_hub_id", nullable = false)
    private UUID startHubId;

    @Column(name = "end_hub_id", nullable = false)
    private UUID endHubId;

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
        if(requestDto.getStartHubId() != null) this.startHubId = requestDto.getStartHubId();
        if(requestDto.getEndHubId() != null) this.endHubId = requestDto.getEndHubId();
        if(requestDto.getDeliveryManagerId() != null) this.deliveryManagerId = requestDto.getDeliveryManagerId();
        if(requestDto.getReceiverCompanyId() != null) this.receiverCompanyId = requestDto.getReceiverCompanyId();
        if(StringUtil.isNullOrEmpty(requestDto.getReceiverCompanySlackId())) this.receiverCompanySlackId = requestDto.getReceiverCompanySlackId();
    }

}
