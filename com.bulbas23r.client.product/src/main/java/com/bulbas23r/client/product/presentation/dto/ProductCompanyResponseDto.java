package com.bulbas23r.client.product.presentation.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCompanyResponseDto {
    private UUID id;
    private UUID hubId;
    private UUID managerId;
    private String name;
    private String type;
    private String phone;
    private String roadAddress;
    private String jibunAddress;
    private String city;
    private String district;
    private String town;
    private String postalCode;
}
