package com.bulbas23r.client.deliverymanager.presentation.dto;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManagerType;
import common.annotation.ValidEnum;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateDeliveryManagerRequestDto {

    @NotNull
    private Long userId;
    private UUID hubId;
    @ValidEnum(enumClass = DeliveryManagerType.class)
    private DeliveryManagerType deliveryManagerType;
}
