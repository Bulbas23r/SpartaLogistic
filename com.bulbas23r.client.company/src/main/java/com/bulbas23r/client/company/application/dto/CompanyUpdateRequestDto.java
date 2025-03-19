package com.bulbas23r.client.company.application.dto;

import com.bulbas23r.client.company.domain.model.CompanyType;
import common.annotation.ValidEnum;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CompanyUpdateRequestDto {

    @ValidEnum(enumClass = CompanyType.class)
    private CompanyType type;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 양식에 맞지 않습니다.")
    private String phone;

    private String roadAddress;
    private String jibunAddress;
    private String city;
    private String district;
    private String town;
    private String postalCode;
}
