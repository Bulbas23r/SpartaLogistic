package com.bulbas23r.client.deliverymanager.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDeliveryManagerRequestDto {

    @NotNull
    private Integer sequence;
}
