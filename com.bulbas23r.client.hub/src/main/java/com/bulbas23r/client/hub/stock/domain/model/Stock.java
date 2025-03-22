package com.bulbas23r.client.hub.stock.domain.model;

import common.event.CreateStockEventDto;
import common.model.BaseEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted is false")
@Table(name = "p_stock")
public class Stock extends BaseEntity {

    @EmbeddedId
    private StockId id;

    private Integer quantity;

    public static Stock fromEventDto(CreateStockEventDto eventDto) {
        return Stock.builder()
            .id(new StockId(eventDto.getHubId(), eventDto.getProductId()))
            .quantity(eventDto.getQuantity())
            .build();
    }
}
