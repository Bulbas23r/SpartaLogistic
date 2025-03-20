package com.bulbas23r.client.message.controller;

import com.bulbas23r.client.message.MessageResponseDto;
import com.bulbas23r.client.message.PostMessageDto;
import com.bulbas23r.client.message.SlackServiceImpl;
import common.annotation.RoleCheck;
import common.utils.PageUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    slackService.sendDirectMessage(postMessageDto);
    return ResponseEntity.ok("메세지를 전송했습니다.");
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<?> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody String message
  ) {
    slackService.updateMessage(messageId,message);
    return ResponseEntity.ok("메세지를 수정했습니다.");
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<?> deleteMessage(@PathVariable UUID messageId) {
    slackService.deleteMessage(messageId);
    return ResponseEntity.ok("메세지가 삭제되었습니다.");
  }

  @GetMapping("/{messageId}")
  public ResponseEntity<?> getMessage(@PathVariable UUID messageId) {
    MessageResponseDto result  = slackService.getMessage(messageId);
    return ResponseEntity.ok(result);
  }

  @RoleCheck("MASTER")
  @GetMapping("/list")
  public ResponseEntity<?> getMessages(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size
  ) {
    Pageable pageable = PageUtils.pageable(page, size);
    Page<MessageResponseDto> result = slackService.getMessageList(pageable);

    return ResponseEntity.ok(result);
  }

  // todo 메세지 검색
}
