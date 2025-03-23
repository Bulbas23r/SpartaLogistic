//package com.bulbas23r.client.message.infrastructure.messaging;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class OrderEventConsumer {
//  private final KafkaTemplate<String, Object> kafkaTemplate;
//  private final ObjectMapper objectMapper;
//
//  // 상품 및 수량 정보, 주문 요청 사항, 허브 ID
//  @KafkaListener(topics = "order-created")
//}
