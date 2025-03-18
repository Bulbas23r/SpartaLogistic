package com.bulbas23r.client.company.domain.repository;

import com.bulbas23r.client.company.application.dto.CompanyResponseDto;
import com.bulbas23r.client.company.domain.model.CompanyType;
import common.utils.PageUtils.CommonSortBy;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface CompanyQueryRepository {

    Page<CompanyResponseDto> searchCompany(String name, UUID hubId , CompanyType type, Pageable pageable,
        Direction sortDirection, CommonSortBy sortBy);
}
