package com.bulbas23r.client.deliverymanager.domain.model;

import com.bulbas23r.client.deliverymanager.presentation.dto.CreateDeliveryManagerRequestDto;
import com.bulbas23r.client.deliverymanager.presentation.dto.UpdateDeliveryManagerRequestDto;
import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
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
@Table(name = "p_delivery_manager")
public class DeliveryManager extends BaseEntity {

    @Id
    private Long userId;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryManagerType type;

    @Column(nullable = false)
    private Integer sequence;

    public DeliveryManager(CreateDeliveryManagerRequestDto requestDto) {
        this.userId = requestDto.getUserId();
        this.hubId = requestDto.getHubId();
        this.slackId = requestDto.getSlackId();
        this.type = requestDto.getDeliveryManagerType();
        this.sequence = requestDto.getSequence();
    }

    public void update(UpdateDeliveryManagerRequestDto requestDto) {
    }
}
