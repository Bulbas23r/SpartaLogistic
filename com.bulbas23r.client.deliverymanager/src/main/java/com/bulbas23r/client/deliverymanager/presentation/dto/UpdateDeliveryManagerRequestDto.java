package com.bulbas23r.client.deliverymanager.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDeliveryManagerRequestDto {

    @NotBlank
    private String slackId;
    @NotNull
    private Integer sequence;
}
