package com.bulbas23r.client.message.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.bulbas23r.client.message.application.service.message.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import common.UserContextHolder;
import common.dto.HubInfoResponseDto;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "hub-service.url=http://localhost:${wiremock.server.port}/api/hubs",
    "slack.bot.token=test-token"
})
class HubClientTest {

  @MockitoBean
  private MessageService messageService;  // 문제되는 Bean Mock 처리

  @Autowired
  private HubClient hubClient;

  @Autowired
  private WireMockServer wireMockServer;

  @Test
  void testGetHubInfo() throws JsonProcessingException {
    // 테스트에 사용할 hubId 생성
    UUID hubId = UUID.randomUUID();

    UserContextHolder.setCurrentUser(
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImRsZWhkZ2siLCJyb2xlIjoiTUFTVEVSIiwiaWF0IjoxNzQyNDUxMjE3LCJleHAiOjE3NDI0NTQ4MTd9.9Af1z-MbDVQ4FzYMMva5PVUmYWgRAhI5c1K3PL_kGF0"
                    ,"testUser"
                    ,"MASTER"
        );

    // WireMock으로 목 응답 설정
    wireMockServer
        .stubFor(get(urlEqualTo("/api/hubs/" + hubId))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody("{"
                + "\"id\":\"" + hubId + "\","
                + "\"name\":\"Mocked Hub\","
                + "\"managerId\":123,"
                + "\"roadAddress\":\"Mocked Road 123\","
                + "\"jibunAddress\":\"Mocked Jibun 456\","
                + "\"city\":\"Mocked City\","
                + "\"district\":\"Mocked District\","
                + "\"town\":\"Mocked Town\","
                + "\"postalCode\":\"99999\","
                + "\"latitude\":37.0,"
                + "\"longitude\":127.0"
                + "}")));


    // 결과 검증
// HubClient를 통해 호출하여 결과 검증
    ResponseEntity<HubInfoResponseDto> response = hubClient.getHubInfo(hubId);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    HubInfoResponseDto dto = response.getBody();
    assertNotNull(dto);
    assertEquals("Mocked Hub", dto.getName());
    assertEquals(123L, dto.getManagerId());
    assertEquals("Mocked Road 123", dto.getRoadAddress());
    assertEquals("Mocked Jibun 456", dto.getJibunAddress());
    assertEquals(37.0, dto.getLatitude());
    assertEquals(127.0, dto.getLongitude());
  }
}