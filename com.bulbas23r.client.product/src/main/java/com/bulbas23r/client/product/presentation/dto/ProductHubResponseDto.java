package com.bulbas23r.client.product.presentation.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class ProductHubResponseDto {
    private UUID id;
    private String name;
    private Long managerId;
    private String roadAddress;
    private String jibunAddress;
    private String city;
    private String district;
    private String town;
    private String postalCode;
    private Double latitude;
    private Double longitude;
}
