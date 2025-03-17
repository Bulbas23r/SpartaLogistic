package com.bulbas23r.client.company.application.service;

import com.bulbas23r.client.company.application.dto.CompanyCreateRequestDto;
import com.bulbas23r.client.company.application.dto.CompanyResponseDto;
import com.bulbas23r.client.company.application.dto.CompanyUpdateRequestDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    CompanyResponseDto createCompany(CompanyCreateRequestDto requestDto);
    CompanyResponseDto updateCompany(UUID companyId, CompanyUpdateRequestDto requestDto);
    CompanyResponseDto getCompanyDetail(UUID id);
    CompanyResponseDto deleteCompany(UUID id);
    Page<CompanyResponseDto>  getCompanyList(Pageable pageable);
}
