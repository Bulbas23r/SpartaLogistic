package com.bulbas23r.client.message.infrastructure.messaging;

import com.bulbas23r.client.message.application.service.gemini.GeminiService;
import com.bulbas23r.client.message.application.service.message.MessageService;
import com.bulbas23r.client.message.client.DeliveryClient;
import com.bulbas23r.client.message.client.HubClient;
import com.bulbas23r.client.message.client.UserClient;
import com.bulbas23r.client.message.presentation.dto.QuestionRequestDto;
import com.bulbas23r.client.message.presentation.dto.request.PostMessageDto;
import com.bulbas23r.client.message.presentation.dto.response.DeliveryResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.HubInfoResponseDto;
import common.dto.UserInfoResponseDto;
import common.event.OrderMessageEventDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final DeliveryClient deliveryClient;
  private final HubClient hubClient;
  private final UserClient userClient;
  private final GeminiService geminiService;
  private final MessageService messageService;

  @KafkaListener(topics = "create-order")
  public void handleEvent(Map<String, Object> eventMap) {
    OrderMessageEventDto event = objectMapper.convertValue(eventMap, OrderMessageEventDto.class);

    // 1. 주문 아이디로 배달 정보 가져오기
    ResponseEntity<Page<DeliveryResponseDto>> deliveryResponseEntity =
        deliveryClient.getDeliveryByOrderIdRouteList(event.getOrderId());

    if (deliveryResponseEntity.getStatusCode().is2xxSuccessful() &&
        deliveryResponseEntity.getBody() != null) {

      List<DeliveryResponseDto> routes = deliveryResponseEntity.getBody().getContent();
      if (!routes.isEmpty()) {
        // 출발 허브: 리스트 첫번째 요소의 departureHubId
        UUID departureHubId = routes.get(0).getDepartureHubId();
        // 도착 허브: 리스트 마지막 요소의 arrivalHubId
        UUID arrivalHubId = routes.get(routes.size() - 1).getArrivalHubId();

        // 경유 허브: 첫번째와 마지막을 제외한 나머지 요소의 departureHubId
        List<UUID> transitHubIds = new ArrayList<>();
        if (routes.size() > 2) {
          for (int i = 1; i < routes.size() - 1; i++) {
            transitHubIds.add(routes.get(i).getDepartureHubId());
          }
        }

        // 각 허브 ID로 허브 정보 조회
        String departureHubName = "";
        String hubSlackId = "";
        ResponseEntity<HubInfoResponseDto> departureResponse = hubClient.getHubInfo(departureHubId);
        if (departureResponse.getStatusCode().is2xxSuccessful()
            && departureResponse.getBody() != null) {

          departureHubName = departureResponse.getBody().getName();

          hubSlackId = userClient.getUser(
              departureResponse.getBody().getManagerId()).getBody().getSlackId();
        }

        String arrivalHubName = "";
        ResponseEntity<HubInfoResponseDto> arrivalResponse = hubClient.getHubInfo(arrivalHubId);
        if (arrivalResponse.getStatusCode().is2xxSuccessful()
            && arrivalResponse.getBody() != null) {
          arrivalHubName = arrivalResponse.getBody().getName();
        }

        List<String> transitHubNames = new ArrayList<>();
        for (UUID transitId : transitHubIds) {
          ResponseEntity<HubInfoResponseDto> transitResponse = hubClient.getHubInfo(transitId);
          if (transitResponse.getStatusCode().is2xxSuccessful()
              && transitResponse.getBody() != null) {
            transitHubNames.add(transitResponse.getBody().getName());
          }
        }

        // 4. 전체 정보를 기반으로 질의할 내용을 구성 (실제 DTO에 허브명 반영)
        QuestionRequestDto questionRequestDto = new QuestionRequestDto(
            event,
            departureHubName,
            transitHubNames,
            arrivalHubName
        );

        // 5. 메시지 서비스의 AI를 호출하여 응답 받기
        String answer = geminiService.getAi(toAnswer(questionRequestDto));

        // 6. 응답을 허브 담당자 슬랙으로 전송
        messageService.sendDirectMessage(new PostMessageDto(hubSlackId, answer));
      }
    }
  }

  private String toAnswer(QuestionRequestDto requestDto) {
    // 주문 이벤트 내 상품 정보들을 "상품명: 수량개" 형태의 문자열로 변환
    String productString = requestDto.getOrderMessageEventDto().getProducts().stream()
        .map(product -> product.getProductName() + ": " + product.getQuantity() + "개")
        .collect(Collectors.joining(", "));

    // requestDto에 담긴 허브명들을 사용하여 답변 문자열 구성
    return productString + "\n주문 요청 사항 : " + requestDto.getOrderMessageEventDto().getMemo() +
        "\n출발지 : " + requestDto.getDepartureHubName() +
        "\n경유지 : " + (requestDto.getTransitHubNames().isEmpty() ? "없음"
        : String.join(", ", requestDto.getTransitHubNames())) +
        "\n도착지 : " + requestDto.getArriveHubName()
        +"출발지, 경유지, 도착지의 위치를 바탕으로 발송지에서 최종 발송 시한이 언제인지 알려줘";
  }
}
