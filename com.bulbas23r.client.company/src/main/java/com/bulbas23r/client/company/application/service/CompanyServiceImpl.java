package com.bulbas23r.client.company.application.service;

import com.bulbas23r.client.company.application.dto.CompanyUpdateRequestDto;
import com.bulbas23r.client.company.domain.model.CompanyType;
import com.bulbas23r.client.company.domain.repository.CompanyQueryRepository;
import common.annotation.ValidUUID;
import common.exception.NotFoundException;
import common.exception.BadRequestException;
import com.bulbas23r.client.company.application.dto.CompanyCreateRequestDto;
import com.bulbas23r.client.company.application.dto.CompanyResponseDto;
import com.bulbas23r.client.company.domain.model.Company;
import com.bulbas23r.client.company.domain.repository.CompanyRepository;
import common.utils.PageUtils.CommonSortBy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyQueryRepository companyQueryRepository;

    @Transactional
    public CompanyResponseDto createCompany(CompanyCreateRequestDto companyRequestDto) {
        //todo: 허브id, 매니저 id 유효한지 check

        if(isCompanyNameExistInHub(companyRequestDto.getHub_id(), companyRequestDto.getName())) {
          throw new BadRequestException("하나의 허브에 업체는 중복 될 수 없습니다.");
        }

        Company company = companyRequestDto.toCompany();
        companyRepository.save(company);

        return CompanyResponseDto.fromEntity(company);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponseDto> getCompanyList(Pageable pageable) {
        return companyRepository.findAllByIsDeletedIsFalse(pageable).map(CompanyResponseDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyDetail(UUID id) {
        return CompanyResponseDto.fromEntity(getCompany(id));
    }

    @Transactional
    public CompanyResponseDto updateCompany(UUID companyId, CompanyUpdateRequestDto requestDto) {
        Company company = getCompany(companyId);
        company.update(requestDto);

        return CompanyResponseDto.fromEntity(company);
    }

    @Transactional
    public CompanyResponseDto deleteCompany(UUID id) {
        Company company = getCompany(id);
        company.setDeleted();

        return CompanyResponseDto.fromEntity(company);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponseDto> searchCompany(String name,UUID hubId, CompanyType type ,Pageable pageable, Direction sortDirection, CommonSortBy sortBy){
        return companyQueryRepository.searchCompany(name,hubId,type,pageable,sortDirection,sortBy);
    }

    public Company getCompany(UUID id) {
        return companyRepository.findById(id).orElseThrow(()-> new NotFoundException("업체를 찾을 수 없습니다."));
    }

    public boolean isCompanyNameExistInHub(UUID hubId, String name) {
        return companyRepository.existsByHubIdAndName(hubId,name);
    }
}
