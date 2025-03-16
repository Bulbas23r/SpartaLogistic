package com.bulbas23r.client.hub.hub.domain.model;

import com.bulbas23r.client.hub.hub.presentation.dto.request.CreateHubRequestDto;
import com.bulbas23r.client.hub.hub.presentation.dto.request.UpdateHubRequestDto;
import common.model.BaseEntity;
import jakarta.persistence.Column;
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

@Getter
@Setter
@Entity
@Table(name = "p_hub")
@NoArgsConstructor
@AllArgsConstructor
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    private Long managerId;

    @Column(nullable = false)
    private String roadAddress;
    @Column(nullable = false)
    private String jibunAddress;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String town;
    @Column(nullable = false)
    private String postalCode;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;

    public Hub(CreateHubRequestDto requestDto) {
        this.name = requestDto.getName();
        this.managerId = requestDto.getManagerId();
        this.roadAddress = requestDto.getRoadAddress();
        this.jibunAddress = requestDto.getJibunAddress();
        this.city = requestDto.getCity();
        this.district = requestDto.getDistrict();
        this.town = requestDto.getTown();
        this.postalCode = requestDto.getPostalCode();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
    }

    public void update(UpdateHubRequestDto requestDto) {
        this.name = requestDto.getName();
        this.managerId = requestDto.getManagerId();
        this.roadAddress = requestDto.getRoadAddress();
        this.jibunAddress = requestDto.getJibunAddress();
        this.city = requestDto.getCity();
        this.district = requestDto.getDistrict();
        this.town = requestDto.getTown();
        this.postalCode = requestDto.getPostalCode();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
    }
}
