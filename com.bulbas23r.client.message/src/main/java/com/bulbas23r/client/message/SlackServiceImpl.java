package com.bulbas23r.client.message;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackServiceImpl {

  private final RestTemplate restTemplate;
  private final MessageRepository messageRepository;

  @Value("${slack.incoming-hook.url}")
  String slackUrl;

  public void sendMessage(PostMessageDto postMessageDto) {
    try{
      SlackIncomingHookDto request = new SlackIncomingHookDto("@"+postMessageDto.getSlackId(), postMessageDto.getMessage());
      log.info(restTemplate.postForObject(slackUrl, request, String.class));
//      return MessageResponseDto.from(messageRepository.save(postMessageDto.toEntity(LocalDateTime.now())));
    }catch (Exception e){
      log.error("Internal Server Error");
//      throw new CoreApiException(ErrorType.DEFAULT_ERROR);
    }
  }
}
