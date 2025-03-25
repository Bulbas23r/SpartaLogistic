package com.bulbas23r.client.company.presentation.controller;

import com.bulbas23r.client.company.application.service.CompanyService;
import com.bulbas23r.client.company.domain.model.Company;
import com.bulbas23r.client.company.domain.model.CompanyType;
import com.bulbas23r.client.company.presentation.dto.CompanyCreateRequestDto;
import com.bulbas23r.client.company.presentation.dto.CompanyResponseDto;
import com.bulbas23r.client.company.presentation.dto.CompanyUpdateRequestDto;
import common.annotation.RoleCheck;
import common.utils.PageUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @RoleCheck({"MASTER", "HUB_MANAGER"})
    @PostMapping
    public ResponseEntity<?> createCompany(@RequestBody CompanyCreateRequestDto requestDto) {
        CompanyResponseDto resultDto = companyService.createCompany(requestDto);
        return ResponseEntity.ok(resultDto);
    }

    @RoleCheck({"MASTER", "HUB_MANAGER, COMPANY"})
    @PutMapping("/{companyId}")
    public ResponseEntity<?> updateCompany(@PathVariable("companyId") UUID companyId,
        @RequestBody CompanyUpdateRequestDto requestDto) {
        CompanyResponseDto resultDto = companyService.updateCompany(companyId, requestDto);
        return ResponseEntity.ok(resultDto);
    }

    @GetMapping
    public ResponseEntity<?> getCompanyList(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<CompanyResponseDto> result = companyService.getCompanyList(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<?> getCompany(@PathVariable("companyId") UUID companyId) {
        CompanyResponseDto resultDto = companyService.getCompanyDetail(companyId);
        return ResponseEntity.ok(resultDto);
    }

    @RoleCheck({"MASTER", "HUB_MANAGER"})
    @DeleteMapping("/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable("companyId") UUID companyId) {
        CompanyResponseDto resultDto = companyService.deleteCompany(companyId);
        return ResponseEntity.ok(resultDto);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCompany(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "hubId", required = false) UUID hubId,
        @RequestParam(name = "type", required = false) CompanyType type,
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        @RequestParam(name = "sortDirection", defaultValue = "DESC", required = false) Sort.Direction sortDirection,
        @RequestParam(name = "sortBy", defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy
    ) {
        Pageable pageable = PageUtils.pageable(page, size);
        Page<CompanyResponseDto> result = companyService.searchCompany(name, hubId, type, pageable,
            sortDirection, sortBy);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/info/id/{companyId}")
    public ResponseEntity<?> getCompanyInfoById(@PathVariable("companyId") UUID companyId) {
        Company company = companyService.getCompany(companyId);

        return ResponseEntity.ok(company.toCompanyInfoResponseDto());
    }

}
