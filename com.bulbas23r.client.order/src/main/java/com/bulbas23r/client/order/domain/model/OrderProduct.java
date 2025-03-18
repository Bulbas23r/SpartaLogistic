package com.bulbas23r.client.order.domain.model;

import com.bulbas23r.client.order.presentation.dto.OrderProductCreateRequestDto;
import com.bulbas23r.client.order.presentation.dto.OrderProductUpdateRequestDto;
import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "p_order_product")
public class OrderProduct extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * 주문 아이디 (order 테이블과 다대일 매핑)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * 허브 아이디
     */
    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    /**
     * 제품 아이디
     */
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    /**
     * 주문 제품 이름
     */
    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    /**
     * 주문 수량
     */
    @Column(name = "quantity", nullable = false)
    private int quantity;

    /**
     * 주문 총 금액
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 주문 요청사항
     */
    @Column(name = "memo")
    private String memo;

    public OrderProduct(OrderProductCreateRequestDto dto) {
        super();
        this.hubId = dto.getHubId();
        this.productId = dto.getProductId();
        this.productName = dto.getProductName();
        this.quantity = dto.getQuantity();
        this.price = dto.getPrice();
        this.memo = dto.getMemo();
    }
}
