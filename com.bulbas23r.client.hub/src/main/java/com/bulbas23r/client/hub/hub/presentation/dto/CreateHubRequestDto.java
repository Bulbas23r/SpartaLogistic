package com.bulbas23r.client.hub.hub.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateHubRequestDto {

    @NotBlank
    private String name;
    private Long managerId;
    @NotBlank
    private String roadAddress;
    @NotBlank
    private String jibunAddress;
    @NotBlank
    private String city;
    @NotBlank
    private String district;
    @NotBlank
    private String town;
    @NotBlank
    private String postalCode;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
