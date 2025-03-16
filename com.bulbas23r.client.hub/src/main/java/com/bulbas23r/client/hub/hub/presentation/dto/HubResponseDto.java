package com.bulbas23r.client.hub.hub.presentation.dto;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import java.util.UUID;
import lombok.Data;

@Data
public class HubResponseDto {

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

    public HubResponseDto(Hub hub) {
        this.id = hub.getId();
        this.name = hub.getName();
        this.managerId = hub.getManagerId();
        this.roadAddress = hub.getRoadAddress();
        this.jibunAddress = hub.getJibunAddress();
        this.city = hub.getCity();
        this.district = hub.getDistrict();
        this.town = hub.getTown();
        this.postalCode = hub.getPostalCode();
        this.latitude = hub.getLatitude();
        this.longitude = hub.getLongitude();
    }
}
