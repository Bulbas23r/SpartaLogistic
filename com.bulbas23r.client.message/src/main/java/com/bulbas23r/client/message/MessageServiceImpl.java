//package com.bulbas23r.client.message;
//
//import java.util.HashMap;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//@RequiredArgsConstructor
//public class MessageServiceImpl {
//
//  @Value("${slackBotToken}")
//  private String slackToken;
//
//  private final RestTemplate restTemplate = new RestTemplate();
//
//  public void sendMessageToUser(PostMessageDto dto) {
//    // 1. DM 채널 열기
//    String openUrl = "https://slack.com/api/conversations.open";
//    HttpHeaders headers = new HttpHeaders();
//    headers.setBearerAuth(slackToken);
//    headers.setContentType(MediaType.APPLICATION_JSON);
//
//    Map<String, Object> openPayload = new HashMap<>();
//    // DM을 보낼 사용자 ID (U로 시작하는)
//    openPayload.put("users", dto.getSlackId());
//
//    HttpEntity<Map<String, Object>> openRequest = new HttpEntity<>(openPayload, headers);
//    ResponseEntity<Map> openResponse = restTemplate.postForEntity(openUrl, openRequest, Map.class);
//
//    if (openResponse.getBody() == null || !Boolean.TRUE.equals(openResponse.getBody().get("ok"))) {
//      System.out.println("Failed to open conversation: " + openResponse.getBody());
//      return;
//    }
//
//    // conversations.open 응답에서 DM 채널 ID 추출
//    Map channelData = (Map) openResponse.getBody().get("channel");
//    String channelId = (String) channelData.get("id");  // D로 시작하는 DM 채널 ID
//
//    // 2. 채널 ID를 이용해 메시지 전송
//    String postUrl = "https://slack.com/api/chat.postMessage";
//    Map<String, Object> messagePayload = new HashMap<>();
//    messagePayload.put("channel", channelId);
//    messagePayload.put("text", dto.getMessage());
//
//    HttpEntity<Map<String, Object>> messageRequest = new HttpEntity<>(messagePayload, headers);
//    ResponseEntity<Map> messageResponse = restTemplate.postForEntity(postUrl, messageRequest, Map.class);
//
//    System.out.println("Message Response: " + messageResponse.getBody());
//  }
//}
