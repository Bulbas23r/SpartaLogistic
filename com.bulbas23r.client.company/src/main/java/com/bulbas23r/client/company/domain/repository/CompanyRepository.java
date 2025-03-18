package com.bulbas23r.client.company.domain.repository;

import com.bulbas23r.client.company.domain.model.Company;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepository {
    Company save(Company company);
    boolean existsByHubIdAndName(UUID hubId, String name);
    Optional<Company> findById(UUID id);
    Page<Company> findAllByIsDeletedIsFalse(Pageable pageable);
}
