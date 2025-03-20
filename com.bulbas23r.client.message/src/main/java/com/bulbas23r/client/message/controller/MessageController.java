package com.bulbas23r.client.message.controller;

import com.bulbas23r.client.message.PostMessageDto;
import com.bulbas23r.client.message.SlackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

//  private final MessageServiceImpl messageService;
//
//  // todo 메세지 생성(발송)
//  @PostMapping()
//  public ResponseEntity<String> sendMessage(@RequestBody PostMessageDto dto) {
//    messageService.sendMessageToUser(dto);
//    return ResponseEntity.ok("Message sent");
//  }

  private final SlackServiceImpl slackService;

  @PostMapping
  public ResponseEntity<?> postMessage(@RequestBody PostMessageDto postMessageDto) {
    slackService.sendMessage(postMessageDto);
    return ResponseEntity.ok().build();
  }

  // todo 메세지 수정
  // todo 메세지 삭제
  // todo 메세지 단건 조회
  // todo 메세지 리스트 검색
  // todo 메세지 검색
}
