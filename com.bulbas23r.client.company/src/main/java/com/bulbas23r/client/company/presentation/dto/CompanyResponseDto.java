package com.bulbas23r.client.company.presentation.dto;

import com.bulbas23r.client.company.domain.model.Company;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {
    private UUID id;
    private UUID hubId;
    private Long managerId;
    private String name;
    private String type;
    private String phone;
    private String roadAddress;
    private String jibunAddress;
    private String city;
    private String district;
    private String town;
    private String postalCode;

    public static CompanyResponseDto fromEntity(Company company) {
        return CompanyResponseDto.builder()
            .id(company.getId())
            .hubId(company.getHubId())
            .managerId(company.getManagerId())
            .name(company.getName())
            .type(company.getType().toString())
            .phone(company.getPhone())
            .roadAddress(company.getAddress().getRoadAddress())
            .jibunAddress(company.getAddress().getJibunAddress())
            .city(company.getAddress().getCity())
            .district(company.getAddress().getDistrict())
            .town(company.getAddress().getTown())
            .postalCode(company.getAddress().getPostalCode())
            .build();
    }
}
