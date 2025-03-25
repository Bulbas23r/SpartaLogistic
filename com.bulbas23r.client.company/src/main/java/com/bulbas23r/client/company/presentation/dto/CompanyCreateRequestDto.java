package com.bulbas23r.client.company.presentation.dto;

import com.bulbas23r.client.company.domain.model.Company;
import com.bulbas23r.client.company.domain.model.CompanyType;
import common.annotation.ValidEnum;
import common.annotation.ValidUUID;
import common.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CompanyCreateRequestDto {

    @NotBlank(message = "업체명을 입력해 주세요.")
    private String name;

    @ValidUUID
    private UUID hubId;

    @NotBlank(message = "업체 담당자를 입력해 주세요.")
    private Long managerId;

    @ValidEnum(enumClass = CompanyType.class)
    private CompanyType type;

    @NotBlank(message = "업체 전화번호를 입력해 주세요.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 양식에 맞지 않습니다.")
    private String phone;

    @NotBlank(message = "도로명 주소를 입력해 주세요.")
    private String roadAddress;

    private String jibunAddress;

    @NotBlank(message = "도시를 입력해 주세요.")
    private String city;
    private String district;

    @NotBlank(message = "시/군 을 입력해 주세요.")
    private String town;

    @NotBlank(message = "우편번호를 입력해 주세요.")
    private String postalCode;

    public Company toCompany() {
        Address address = Address.
            builder()
            .roadAddress(roadAddress)
            .jibunAddress(jibunAddress)
            .city(city)
            .district(district)
            .town(town)
            .postalCode(postalCode)
            .build();

        return Company.builder()
            .hubId(hubId)
            .managerId(managerId)
            .name(name)
            .type(type)
            .phone(phone)
            .address(address)
            .build();
    }

}
