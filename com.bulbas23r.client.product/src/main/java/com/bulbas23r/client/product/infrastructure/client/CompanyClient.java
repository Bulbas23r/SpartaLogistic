package com.bulbas23r.client.product.infrastructure.client;

import com.bulbas23r.client.product.presentation.dto.ProductCompanyResponseDto;
import common.config.FeignConfig;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "http://localhost:19091/api/companies", configuration = FeignConfig.class)
public interface CompanyClient {
    @GetMapping("/{companyId}")
    ProductCompanyResponseDto getCompany(@PathVariable("companyId") UUID companyId);
}
