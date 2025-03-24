package com.bulbas23r.client.message.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.HubInfoResponseDto;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    properties = {
        "spring.main.web-application-type=none",
        "spring.main.allow-bean-definition-overriding=true",
        "spring.cloud.openfeign.enabled=false"
    }
)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 19093)
class HubClientTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private com.bulbas23r.client.message.client.HubClient hubClient;

  @Test
  void testGetHubInfo() {
    // 테스트에 사용할 hubId 생성
    UUID hubId = UUID.randomUUID();

    // WireMock을 통해 GET /{hubId} 요청에 대한 모의 응답 설정
    String jsonResponse = "{"
        + "\"id\": \"" + hubId + "\","
        + "\"name\": \"Test Hub\","
        + "\"managerId\": 1,"
        + "\"roadAddress\": \"123 Road\","
        + "\"jibunAddress\": \"456 Jibun\","
        + "\"city\": \"Test City\","
        + "\"district\": \"Test District\","
        + "\"town\": \"Test Town\","
        + "\"postalCode\": \"12345\","
        + "\"latitude\": 37.0,"
        + "\"longitude\": 127.0"
        + "}";

    stubFor(get(urlEqualTo("/api/hubs/" + hubId))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody(jsonResponse)));

    // Feign 클라이언트를 통해 호출하고 응답 검증
    ResponseEntity<HubInfoResponseDto> response = hubClient.getHubInfo(hubId);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    HubInfoResponseDto dto = response.getBody();
    assertNotNull(dto);
    assertEquals("Test Hub", dto.getName());
    assertEquals(1L, dto.getManagerId());
    assertEquals("123 Road", dto.getRoadAddress());
    assertEquals("456 Jibun", dto.getJibunAddress());
    assertEquals(37.0, dto.getLatitude());
    assertEquals(127.0, dto.getLongitude());
  }
}