package com.bulbas23r.client.message.application.service;

import com.bulbas23r.client.message.client.UserClient;
import com.bulbas23r.client.message.domain.repository.MessageQueryRepository;
import com.bulbas23r.client.message.domain.repository.MessageRepository;
import com.bulbas23r.client.message.presentation.dto.response.MessageResponseDto;
import com.bulbas23r.client.message.presentation.dto.request.PostMessageDto;
import com.bulbas23r.client.message.presentation.dto.SlackIncomingHookDto;
import com.bulbas23r.client.message.domain.model.Message;
import common.UserContextHolder;
import common.exception.BadRequestException;
import common.exception.NotFoundException;
import common.utils.PageUtils.CommonSortBy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService{

  private final RestTemplate restTemplate;
  private final MessageRepository messageRepository;
  private final MessageQueryRepository messageQueryRepository;
  private final UserClient userClient;

  @Value("${slack.bot.token}")
  private String slackBotToken;

  @Transactional
  @Override
  public void sendDirectMessage(PostMessageDto dto) {
    String openUrl = "https://slack.com/api/conversations.open";
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(slackBotToken);
    headers.setContentType(MediaType.valueOf("application/json; charset=utf-8"));

    Map<String, Object> openPayload = new HashMap<>();
    openPayload.put("users", dto.getSlackId());

    HttpEntity<Map<String, Object>> openRequest = new HttpEntity<>(openPayload, headers);
    ResponseEntity<Map> openResponse = restTemplate.postForEntity(openUrl, openRequest, Map.class);

    Map<String, Object> openResponseBody = openResponse.getBody();
    System.out.println("conversations.open 응답: " + openResponseBody);
    if (openResponseBody == null || !Boolean.TRUE.equals(openResponseBody.get("ok"))) {
      throw new NotFoundException("DM 채널 열기에 실패했습니다: " + openResponseBody);
    }

    // 채널 정보 추출
    Map<String, Object> channelData = (Map<String, Object>) openResponseBody.get("channel");
    String channelId = channelData.get("id").toString();

    // chat.postMessage 호출
    String sender = UserContextHolder.getUser();
    SlackIncomingHookDto slackIncomingHookDto = new SlackIncomingHookDto(sender, dto.getMessage());
    String postUrl = "https://slack.com/api/chat.postMessage";
    Map<String, Object> messagePayload = new HashMap<>();
    messagePayload.put("channel", channelId);
    messagePayload.put("text", slackIncomingHookDto.getText());

    HttpEntity<Map<String, Object>> messageRequest = new HttpEntity<>(messagePayload, headers);
    ResponseEntity<Map> messageResponse = restTemplate.postForEntity(postUrl, messageRequest,
        Map.class);
    Map<String, Object> messageResponseBody = messageResponse.getBody();

    if (messageResponseBody == null || !Boolean.TRUE.equals(messageResponseBody.get("ok"))) {
      throw new BadRequestException("메시지 전송 실패: " + messageResponseBody);
    }

    String username = userClient.getUsername(dto.getSlackId());

    Message message = new Message(sender, username, dto.getMessage(),
        new Date(System.currentTimeMillis()));
    messageRepository.save(message);
  }

  @Transactional
  @Override
  public void updateMessage(UUID messageId, String message) {
    Message findMessage = getFindMessage(messageId);

    findMessage.changeMessage(message);

    messageRepository.save(findMessage);
  }

  @Transactional
  @Override
  public void deleteMessage(UUID messageId) {
    Message findMessage = getFindMessage(messageId);

    findMessage.setDeleted();
  }


  @Transactional(readOnly = true)
  @Override
  public MessageResponseDto getMessage(UUID messageId) {
    Message findMessage = getFindMessage(messageId);

    MessageResponseDto result = new MessageResponseDto(findMessage.getId(),
        findMessage.getReceiver(), findMessage.getSender(), findMessage.getMessage(),
        findMessage.getDate());

    return result;
  }

  @Transactional(readOnly = true)
  @Override
  public Page<MessageResponseDto> getMessageList(Pageable pageable) {
    Page<Message> all = messageRepository.findAll(pageable);
    return all.map(MessageResponseDto::new);
  }

  @Override
  public Page<Message> searchMessage(Pageable pageable, Direction sortDirection,
      CommonSortBy sortBy, String keyword) {
    return messageQueryRepository.search(pageable, sortDirection,
        sortBy, keyword);
  }

  private Message getFindMessage(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 메세지"));
  }
}
