package com.bulbas23r.client.message.infrastructure.messaging;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.bulbas23r.client.message.application.service.gemini.GeminiService;
import com.bulbas23r.client.message.application.service.message.MessageService;
import com.bulbas23r.client.message.client.DeliveryClient;
import com.bulbas23r.client.message.client.HubClient;
import com.bulbas23r.client.message.client.OrderClient;
import com.bulbas23r.client.message.client.ProductClient;
import com.bulbas23r.client.message.client.UserClient;
import com.bulbas23r.client.message.presentation.dto.response.DeliveryResponseDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import common.dto.HubInfoResponseDto;
import common.dto.UserInfoResponseDto;
import common.event.CreateOrderEventDto;
import common.event.OrderProductEventDto;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
    "user-service.url=http://localhost:${wiremock.server.port}/api/users",
    "delivery-service.url=http://localhost:${wiremock.server.port}/api/deliveries",
    "hub-service.url=http://localhost:${wiremock.server.port}/api/hubs"
})
@ActiveProfiles("test")
class OrderEventConsumerTest {
  @MockitoBean
  private MessageService messageService;
  @Autowired
  private DeliveryClient deliveryClient;
  @Autowired
  private HubClient hubClient;
  @Autowired
  private UserClient userClient;

  @Autowired
  private ProductClient productClient;
  @Autowired
  private OrderClient orderClient;
  @Autowired
  private GeminiService geminiService;

  @Autowired
  private WireMockServer wireMockServer;

  @InjectMocks
  private OrderEventConsumer orderEventConsumer;

  @Test
  void handleEvent() {
    // given
    UUID orderId = UUID.randomUUID();
    UUID departureHubId = UUID.randomUUID();
    UUID middleHubId = UUID.randomUUID();
    UUID arrivalHubId = UUID.randomUUID();
    UUID productId = UUID.randomUUID();
    UUID deliveryId = UUID.randomUUID();

    OrderProductEventDto orderProductEventDto = new OrderProductEventDto(productId,1);
    CreateOrderEventDto createOrderEventDto = new CreateOrderEventDto(orderId,null,null,departureHubId,List.of(orderProductEventDto));
    createOrderEventDto.setAuthorization("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3RVc2VyIiwicm9sZSI6Ik1BU1RFUiIsImlhdCI6MTc0MjgzNDE5MiwiZXhwIjoxNzQyODM3NzkyfQ.QAvrfUp83KI2fbj5bV5rpKNKJ5PlyynAQM87Pm1eplo");
    createOrderEventDto.setUsername("username");
    createOrderEventDto.setRole("MASTER");

    // 유저 슬랙 아이디 가져오기
    wireMockServer.stubFor(get(urlEqualTo("/api/users/info/1"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody("{ \"id\": 1, \"username\": \"username\", \"slackId\": \"slackId\", \"role\": \"MASTER\" }")));

    ResponseEntity<UserInfoResponseDto> response = userClient.getUser(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("slackId", response.getBody().getSlackId());


    // 주문 아이디로 배송 정보 가져오기
    wireMockServer.stubFor(get(urlEqualTo("/api/deliveries/order/" + orderId))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody("{\n" +
                "    \"id\": \"" + deliveryId + "\",\n" +
                "    \"departureHubId\": \"" + departureHubId + "\",\n" +
                "    \"arrivalHubId\": \"" + arrivalHubId + "\",\n" +
                "    \"deliveryManagerId\": \"" + UUID.randomUUID() + "\",\n" +
                "    \"sequence\": 1,\n" +
                "    \"estimatedDistance\": 100,\n" +
                "    \"estimatedDuration\": 60,\n" +
                "    \"actualDistance\": 95,\n" +
                "    \"actualDuration\": 55\n" +
                "}")));

    ResponseEntity<DeliveryResponseDto> deliveryResponse = deliveryClient.getDeliveryByOrderId(orderId);
    assertEquals(HttpStatus.OK, deliveryResponse.getStatusCode());

    DeliveryResponseDto delivery = deliveryResponse.getBody();
    assertNotNull(delivery);
    assertEquals(deliveryId, delivery.getId());

    // 배달 아이디로 경로 가져오기
    wireMockServer.stubFor(get(urlEqualTo("/api/deliveries/" + delivery.getId() + "/route"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody("{\n"
                + "  \"content\": [\n"
                + "    {\n"
                + "      \"id\": \"" + delivery.getId() + "\",\n"
                + "      \"departureHubId\": \"" + departureHubId + "\",\n"
                + "      \"arrivalHubId\": \"" + middleHubId + "\",\n"
//                + "      \"deliveryManagerId\": \"d65f3a4c-1234-4567-8901-223344556677\",\n"
                + "      \"sequence\": 1,\n"
                + "      \"estimatedDistance\": 120,\n"
                + "      \"estimatedDuration\": 80,\n"
                + "      \"actualDistance\": 115,\n"
                + "      \"actualDuration\": 75\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"" + delivery.getId() + "\",\n"
                + "      \"departureHubId\": \"" + middleHubId + "\",\n"
                + "      \"arrivalHubId\": \"" + arrivalHubId + "\",\n"
//                + "      \"deliveryManagerId\": \"g67f2a3d-4567-7890-1234-445566778899\",\n"
                + "      \"sequence\": 2,\n"
                + "      \"estimatedDistance\": 150,\n"
                + "      \"estimatedDuration\": 100,\n"
                + "      \"actualDistance\": 145,\n"
                + "      \"actualDuration\": 95\n"
                + "    },\n"
                + "    {\n"
                + "      \"id\": \"" + delivery.getId() + "\",\n"
                + "      \"departureHubId\": \"" + arrivalHubId + "\",\n"
                + "      \"arrivalHubId\": \" " + arrivalHubId + " \",\n"
//                + "      \"deliveryManagerId\": \"j90f4a5e-6789-9012-3456-778899001122\",\n"
                + "      \"sequence\": 3,\n"
                + "      \"estimatedDistance\": 200,\n"
                + "      \"estimatedDuration\": 130,\n"
                + "      \"actualDistance\": 190,\n"
                + "      \"actualDuration\": 120\n"
                + "    }\n"
                + "  ],\n"
                + "  \"totalElements\": 3,\n"
                + "  \"totalPages\": 1,\n"
                + "  \"size\": 10,\n"
                + "  \"number\": 0\n"
                + "}\n")));

    ResponseEntity<Page<DeliveryResponseDto>> deliveryRouteList = deliveryClient.getDeliveryRouteList(
        delivery.getId());

    List<DeliveryResponseDto> routes = deliveryRouteList.getBody().getContent();

    assertEquals(routes.get(0).getDepartureHubId(), departureHubId);


    // 각 허브 아이디로 허브명 조회
    wireMockServer.stubFor(get(urlEqualTo("/api/hubs/" + routes.get(0).getDepartureHubId()))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody("{\n"
                + "  \"name\": \"서울 허브\",\n"
                + "  \"managerId\": 12345,\n"
                + "  \"roadAddress\": \"123 Main Street\",\n"
                + "  \"jibunAddress\": \"123-45\",\n"
                + "  \"latitude\": 37.5665,\n"
                + "  \"longitude\": 126.9780,\n"
                + "  \"active\": true\n"
                + "}")));

    ResponseEntity<HubInfoResponseDto> hubInfo = hubClient.getHubInfo(
        routes.get(0).getDepartureHubId());

    assertEquals(hubInfo.getBody().getName(),"서울 허브");

//
//    when(geminiService.getAi(any())).thenReturn("AI 응답 테스트");
//
//    // when
//    orderEventConsumer.handleEvent(eventMap);

    // then
    // messageService.sendDirectMessage 호출 확인 (필요시 verify 사용 가능)
  }
}