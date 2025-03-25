package com.bulbas23r.client.delivery.presentation.dto.request;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryCompanyRequestDto {

    private UUID deliveryId;

    private Long deliveryManagerId;

}
