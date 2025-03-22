package com.bulbas23r.client.hub.hub.domain.model;

import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import common.dto.HubInfoResponseDto;
import common.model.Address;
import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted is false")
@Table(name = "p_hub")
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true)
    private Long managerId;

    @Column(columnDefinition = "boolean default false")
    private boolean active;

    @Embedded
    private Address address;
    @Embedded
    private Location location;

    public Hub(CreateHubRequestDto requestDto) {
        this.name = requestDto.getName();
        this.managerId = requestDto.getManagerId();
        this.address = new Address(
            requestDto.getRoadAddress(),
            requestDto.getJibunAddress(),
            requestDto.getCity(),
            requestDto.getDistrict(),
            requestDto.getTown(),
            requestDto.getPostalCode()
        );
        this.location = new Location(requestDto.getLatitude(), requestDto.getLongitude());
    }

    public void update(UpdateHubRequestDto requestDto) {
        this.name = requestDto.getName();
        this.managerId = requestDto.getManagerId();
        this.address = new Address(
            requestDto.getRoadAddress(),
            requestDto.getJibunAddress(),
            requestDto.getCity(),
            requestDto.getDistrict(),
            requestDto.getTown(),
            requestDto.getPostalCode()
        );
        this.location = new Location(requestDto.getLatitude(), requestDto.getLongitude());
    }

    public HubInfoResponseDto toHubInfoResponseDto() {
        return HubInfoResponseDto.builder()
            .name(this.name)
            .managerId(this.managerId)
            .jibunAddress(this.address.getJibunAddress())
            .roadAddress(this.address.getRoadAddress())
            .latitude(this.location.getLatitude())
            .longitude(this.location.getLongitude())
            .active(this.active)
            .build();
    }
}
