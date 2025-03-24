package com.bulbas23r.client.company.application.service;

import com.bulbas23r.client.company.domain.model.Company;
import com.bulbas23r.client.company.presentation.dto.CompanyCreateRequestDto;
import com.bulbas23r.client.company.presentation.dto.CompanyResponseDto;
import com.bulbas23r.client.company.presentation.dto.CompanyUpdateRequestDto;
import com.bulbas23r.client.company.domain.model.CompanyType;
import common.utils.PageUtils.CommonSortBy;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface CompanyService {
    CompanyResponseDto createCompany(CompanyCreateRequestDto requestDto);
    CompanyResponseDto updateCompany(UUID companyId, CompanyUpdateRequestDto requestDto);
    CompanyResponseDto getCompanyDetail(UUID id);
    CompanyResponseDto deleteCompany(UUID id);
    Page<CompanyResponseDto>  getCompanyList(Pageable pageable);
    Page<CompanyResponseDto> searchCompany(String name,UUID hubId, CompanyType type ,Pageable pageable, Direction sortDirection, CommonSortBy sortBy);
    Company getCompany(UUID uuid);
}
