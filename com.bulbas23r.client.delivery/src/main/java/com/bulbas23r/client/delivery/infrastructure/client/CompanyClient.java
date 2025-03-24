package com.bulbas23r.client.delivery.infrastructure.client;

import common.config.FeignConfig;
import common.dto.CompanyInfoResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "http://localhost:19091/api/companies", configuration = FeignConfig.class)
public interface CompanyClient {

    @GetMapping("/info/id/{companyId}")
    ResponseEntity<CompanyInfoResponseDto> getCompanyInfoById(@PathVariable("companyId") UUID companyId);

}
