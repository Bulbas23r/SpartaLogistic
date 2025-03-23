package com.bulbas23r.client.message.infrastructure.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bulbas23r.client.message.application.service.gemini.GeminiService;
import com.bulbas23r.client.message.application.service.message.MessageService;
import com.bulbas23r.client.message.client.DeliveryClient;
import com.bulbas23r.client.message.client.HubClient;
import com.bulbas23r.client.message.client.UserClient;
import com.bulbas23r.client.message.presentation.dto.request.PostMessageDto;
import com.bulbas23r.client.message.presentation.dto.response.DeliveryResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.HubInfoResponseDto;
import common.dto.UserInfoResponseDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "spring.cloud.openfeign.enabled=false"  // 실제 Feign 자동 구성을 비활성화
    },
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
@ComponentScan(
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        classes = FeignClient.class  // @FeignClient가 붙은 클래스들은 스캔에서 제외
    )
)
@Import(MocksConfiguration.class)
class OrderEventConsumerTest {

  @Autowired
  private DeliveryClient deliveryClient;

  @Autowired
  private HubClient hubClient;

  @Autowired
  private UserClient userClient;

  @Autowired
  private GeminiService geminiService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private OrderEventConsumer orderEventConsumer;

  @Test
  void testHandleEvent() {
    // 1. 테스트용 이벤트 데이터 생성
    UUID orderId = UUID.randomUUID();
    Map<String, Object> eventMap = new HashMap<>();
    eventMap.put("orderId", orderId.toString());
    eventMap.put("memo", "테스트 주문 메모");
    // 상품 정보 (Java 9+의 Map.of 사용)
    Map<String, Object> product = Map.of("productName", "상품1", "quantity", 1);
    eventMap.put("products", List.of(product));

    // 2. DeliveryClient 목 설정: 배달 경로 정보(Page<DeliveryResponseDto>) 반환
    UUID departureHubId = UUID.randomUUID();
    UUID transitHubId = UUID.randomUUID();
    UUID arrivalHubId = UUID.randomUUID();

    DeliveryResponseDto route1 = new DeliveryResponseDto();
    route1.setDepartureHubId(departureHubId);

    DeliveryResponseDto route2 = new DeliveryResponseDto();
    route2.setDepartureHubId(transitHubId);

    DeliveryResponseDto route3 = new DeliveryResponseDto();
    route3.setArrivalHubId(arrivalHubId);

    List<DeliveryResponseDto> routes = List.of(route1, route2, route3);
    Page<DeliveryResponseDto> page = new PageImpl<>(routes);
    ResponseEntity<Page<DeliveryResponseDto>> deliveryResponseEntity = ResponseEntity.ok(page);
    when(deliveryClient.getDeliveryByOrderIdRouteList(orderId)).thenReturn(deliveryResponseEntity);

    // 3. HubClient 목 설정: 각 허브 ID에 대해 HubInfoResponseDto 반환
    HubInfoResponseDto departureHubInfo = new HubInfoResponseDto();
    departureHubInfo.setName("출발허브");
    // 담당자 정보를 위한 managerId 설정 (여기서는 Long 타입 사용)
    Long managerId = 1L;
    departureHubInfo.setManagerId(managerId);
    ResponseEntity<HubInfoResponseDto> departureResponse = ResponseEntity.ok(departureHubInfo);
    when(hubClient.getHubInfo(departureHubId)).thenReturn(departureResponse);

    HubInfoResponseDto transitHubInfo = new HubInfoResponseDto();
    transitHubInfo.setName("경유허브");
    ResponseEntity<HubInfoResponseDto> transitResponse = ResponseEntity.ok(transitHubInfo);
    when(hubClient.getHubInfo(transitHubId)).thenReturn(transitResponse);

    HubInfoResponseDto arrivalHubInfo = new HubInfoResponseDto();
    arrivalHubInfo.setName("도착허브");
    ResponseEntity<HubInfoResponseDto> arrivalResponse = ResponseEntity.ok(arrivalHubInfo);
    when(hubClient.getHubInfo(arrivalHubId)).thenReturn(arrivalResponse);

    // 4. UserClient 목 설정: 출발 허브 담당자의 Slack ID 반환
    UserInfoResponseDto userInfo = new UserInfoResponseDto();
    userInfo.setSlackId("slack123");
    ResponseEntity<UserInfoResponseDto> userResponse = ResponseEntity.ok(userInfo);
    when(userClient.getUser(managerId)).thenReturn(userResponse);

    // 5. GeminiService 목 설정: AI 응답 메시지 반환
    when(geminiService.getAi(anyString())).thenReturn("AI 응답 메시지");

    // 6. 이벤트 처리 호출
    orderEventConsumer.handleEvent(eventMap);

    // 7. MessageService가 sendDirectMessage를 호출했는지 검증
    ArgumentCaptor<PostMessageDto> captor = ArgumentCaptor.forClass(PostMessageDto.class);
    verify(messageService, times(1)).sendDirectMessage(captor.capture());
    PostMessageDto sentMessage = captor.getValue();

    // 슬랙 아이디 및 메시지 내용에 필요한 허브명과 AI 응답 메시지가 포함되어 있는지 확인
    assertEquals("slack123", sentMessage.getSlackId());
    String messageContent = sentMessage.getMessage();
    assertTrue(messageContent.contains("출발허브"));
    assertTrue(messageContent.contains("경유허브"));
    assertTrue(messageContent.contains("도착허브"));
    assertTrue(messageContent.contains("AI 응답 메시지"));
  }
}