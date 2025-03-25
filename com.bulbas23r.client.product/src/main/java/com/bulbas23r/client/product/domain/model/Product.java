package com.bulbas23r.client.product.domain.model;

import com.bulbas23r.client.product.presentation.dto.ProductCreateRequestDto;
import com.bulbas23r.client.product.presentation.dto.ProductUpdateRequestDto;
import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "decription")
    private String description;

    public Product(ProductCreateRequestDto dto) {
        super();
        this.companyId = dto.getCompanyId();
        this.hubId = dto.getHubId();
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
    }

    public void update(ProductUpdateRequestDto dto){
        this.companyId = dto.getCompanyId();
        this.hubId = dto.getHubId();
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
    }

    public void delete() {
        super.setDeleted(true);
    }
}
