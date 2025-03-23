package com.bulbas23r.client.company.infrastructure.persistence;

import com.bulbas23r.client.company.domain.model.Company;
import com.bulbas23r.client.company.domain.repository.CompanyRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyJpaRepository extends JpaRepository<Company, UUID> , CompanyRepository {
    boolean existsByHubIdAndName(UUID hubId, String name);
    Page<Company> findAll(Pageable pageable);
}
