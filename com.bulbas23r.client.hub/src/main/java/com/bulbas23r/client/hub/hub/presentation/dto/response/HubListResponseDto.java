package com.bulbas23r.client.hub.hub.presentation.dto.response;

import com.bulbas23r.client.hub.hub.domain.model.Hub;
import java.util.UUID;
import lombok.Data;

@Data
public class HubListResponseDto {

    private UUID id;
    private String name;
    private Long managerId;
    private String roadAddress;
    private String jibunAddress;

    public HubListResponseDto(Hub hub) {
        this.id = hub.getId();
        this.name = hub.getName();
        this.managerId = hub.getManagerId();
        this.roadAddress = hub.getAddress().getRoadAddress();
        this.jibunAddress = hub.getAddress().getJibunAddress();
    }
}
