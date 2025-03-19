package com.bulbas23r.client.order.domain.model;

import com.bulbas23r.client.order.presentation.dto.OrderCreateRequestDto;
import com.bulbas23r.client.order.presentation.dto.OrderProductCreateRequestDto;
import com.bulbas23r.client.order.presentation.dto.OrderUpdateRequestDto;
import common.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "p_order")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * 공급 업체 아이디
     */
    @Column(name = "provide_company_id", nullable = false)
    private UUID provideCompanyId;

    /**
     * 주문 수령 업체 아이디
     */
    @Column(name = "receive_company_id", nullable = false)
    private UUID receiveCompanyId;

    /**
     * 주문 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "OrderStatus default 'CREATED'")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OrderStatus status;

    /**
     * 주문에 포함된 주문 제품들 (OrderProduct와 일대다 맵핑)
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderProduct> orderProducts;

    public Order(OrderCreateRequestDto dto) {
        super();
        this.provideCompanyId = dto.getProvideCompanyId();
        this.receiveCompanyId = dto.getReceiveCompanyId();
        this.status = OrderStatus.valueOf(dto.getStatus());
        this.orderProducts = new HashSet<>();
        List<OrderProductCreateRequestDto> orderProductList = dto.getOrderProducts();
        for(OrderProductCreateRequestDto orderProductDto : orderProductList) {
            OrderProduct orderProduct = new OrderProduct(orderProductDto);
            orderProduct.setOrder(this);
            this.orderProducts.add(orderProduct);
        }
    }

    public void update(OrderUpdateRequestDto dto) {
        this.provideCompanyId = dto.getProvideCompanyId();
        this.receiveCompanyId = dto.getReceiveCompanyId();
        this.status = OrderStatus.valueOf(dto.getStatus());
        this.orderProducts = new HashSet<>();
        List<OrderProductCreateRequestDto> orderProductList = dto.getOrderProducts();
        for(OrderProductCreateRequestDto orderProductDto : orderProductList) {
            this.orderProducts.add(new OrderProduct(orderProductDto));
        }
    }
}
