package com.bulbas23r.client.deliverymanager.presentation.dto;

import com.bulbas23r.client.deliverymanager.domain.model.DeliveryManagerType;
import common.annotation.ValidEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateDeliveryManagerRequestDto {

    @NotNull
    private Long userId;
    private UUID hubId;
    @NotBlank
    private String slackId;
    @ValidEnum(enumClass = DeliveryManagerType.class)
    private DeliveryManagerType deliveryManagerType;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer sequence;
}
