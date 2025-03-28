package com.bulbas23r.client.order.domain.model;

public enum OrderStatus {
    CREATED,    // 주문 생성 및 접수
    DELIVERING, // 배달 중
    CANCELLED,  // 주문 취소
    COMPLETED,  // 주문 완료
    FAILED      // 주문 실패
}
