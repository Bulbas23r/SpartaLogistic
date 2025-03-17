package com.bulbas23r.client.hub.hub.presentation.dto.response;

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
        this.roadAddress = hub.getAddress().getRoadAddress();
        this.jibunAddress = hub.getAddress().getJibunAddress();
        this.city = hub.getAddress().getCity();
        this.district = hub.getAddress().getDistrict();
        this.town = hub.getAddress().getTown();
        this.postalCode = hub.getAddress().getPostalCode();
        this.latitude = hub.getLocation().getLatitude();
        this.longitude = hub.getLocation().getLongitude();
    }
}
