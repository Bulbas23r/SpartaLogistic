package com.bulbas23r.client.company.domain.model;

import com.bulbas23r.client.company.application.dto.CompanyUpdateRequestDto;
import common.model.BaseEntity;
import common.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_company")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Company extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "manager_id", nullable = false)
    private UUID managerId;

    @Column(nullable = false , length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyType type;

    @Column(length = 20, nullable = false)
    private String phone;

    @Embedded
    private Address address;

    public void update(CompanyUpdateRequestDto requestDto) {
        this.phone = updateValue(requestDto.getPhone(),phone);
        this.type = updateValue(requestDto.getType(),type);

        this.address = Address.builder()
            .roadAddress(updateValue(requestDto.getRoadAddress(), address.getRoadAddress()))
            .jibunAddress(updateValue(requestDto.getJibunAddress(), address.getJibunAddress()))
            .city(updateValue(requestDto.getCity(),address.getCity()))
            .district(updateValue(requestDto.getDistrict(),address.getDistrict()))
            .town(updateValue(requestDto.getTown(), address.getTown()))
            .postalCode(updateValue(requestDto.getPostalCode(),address.getPostalCode()))
            .build();
    }

    private <T> T updateValue(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

}
