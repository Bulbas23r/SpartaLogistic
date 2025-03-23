package com.bulbas23r.client.company.application.service;

import com.bulbas23r.client.company.infrastructure.client.HubClient;
import com.bulbas23r.client.company.infrastructure.client.UserClient;
import com.bulbas23r.client.company.presentation.dto.CompanyUpdateRequestDto;
import com.bulbas23r.client.company.domain.model.CompanyType;
import com.bulbas23r.client.company.domain.repository.CompanyQueryRepository;
import com.bulbas23r.client.company.infrastructure.messaging.CompanyEventProducer;
import common.dto.HubInfoResponseDto;
import common.dto.UserInfoResponseDto;
import common.exception.NotFoundException;
import common.exception.BadRequestException;
import com.bulbas23r.client.company.presentation.dto.CompanyCreateRequestDto;
import com.bulbas23r.client.company.presentation.dto.CompanyResponseDto;
import com.bulbas23r.client.company.domain.model.Company;
import com.bulbas23r.client.company.domain.repository.CompanyRepository;
import common.model.UserRoleEnum;
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
    private final CompanyEventProducer companyKafkaProducer;
    private final HubClient hubClient;
    private final UserClient userClient;

    @Transactional
    public CompanyResponseDto createCompany(CompanyCreateRequestDto companyRequestDto) {

        HubInfoResponseDto hubInfo = hubClient.getHubInfoById(companyRequestDto.getHubId()).getBody();
        if(hubInfo == null || hubInfo.getActive() == null) {
            throw new BadRequestException("허브가 존재하지 않거나 유효하지 않습니다!");
        }

        UserInfoResponseDto userInfo = userClient.getUserInfo(companyRequestDto.getManagerId())
            .getBody();

        if (userInfo == null || !userInfo.getRole().equals(UserRoleEnum.COMPANY)) {
            throw new BadRequestException("유저가 존재하지 않거나 업체 관리자가 아닙니다!");
        }

        if(isCompanyNameExistInHub(companyRequestDto.getHubId(), companyRequestDto.getName())) {
          throw new BadRequestException("하나의 허브에 업체는 중복 될 수 없습니다.");
        }

        Company company = companyRequestDto.toCompany();
        companyRepository.save(company);

        return CompanyResponseDto.fromEntity(company);
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponseDto> getCompanyList(Pageable pageable) {
        return companyRepository.findAll(pageable).map(CompanyResponseDto::fromEntity);
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

        //event 발행
        companyKafkaProducer.sendDeleteCompanyEvent(id);

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
