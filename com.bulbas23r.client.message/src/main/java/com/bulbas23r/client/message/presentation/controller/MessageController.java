package com.bulbas23r.client.message.presentation.controller;

import com.bulbas23r.client.message.application.service.gemini.GeminiService;
import com.bulbas23r.client.message.application.service.message.MessageService;
import com.bulbas23r.client.message.domain.model.Message;
import com.bulbas23r.client.message.presentation.dto.request.GeminiRequestDto;
import com.bulbas23r.client.message.presentation.dto.response.GeminiResponseDto;
import com.bulbas23r.client.message.presentation.dto.response.MessageResponseDto;
import com.bulbas23r.client.message.presentation.dto.request.PostMessageDto;
import common.annotation.RoleCheck;
import common.utils.PageUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final GeminiService geminiService;

  @PostMapping
  public ResponseEntity<?> postMessage(@RequestBody PostMessageDto postMessageDto) {
    messageService.sendDirectMessage(postMessageDto);
    return ResponseEntity.ok("메세지를 전송했습니다.");
  }

  @RoleCheck("MASTER")
  @PatchMapping("/{messageId}")
  public ResponseEntity<?> updateMessage(
      @PathVariable UUID messageId,
      @RequestBody String message
  ) {
    messageService.updateMessage(messageId,message);
    return ResponseEntity.ok("메세지를 수정했습니다.");
  }

  @RoleCheck("MASTER")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<?> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);
    return ResponseEntity.ok("메세지가 삭제되었습니다.");
  }

  @RoleCheck("MASTER")
  @GetMapping("/{messageId}")
  public ResponseEntity<?> getMessage(@PathVariable UUID messageId) {
    MessageResponseDto result  = messageService.getMessage(messageId);
    return ResponseEntity.ok(result);
  }

  @RoleCheck("MASTER")
  @GetMapping("/list")
  public ResponseEntity<?> getMessages(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size
  ) {
    Pageable pageable = PageUtils.pageable(page, size);
    Page<MessageResponseDto> result = messageService.getMessageList(pageable);

    return ResponseEntity.ok(result);
  }

  @RoleCheck("MASTER")
  @GetMapping("/search")
  public ResponseEntity<?> search(
      @RequestParam(defaultValue = "0", required = false) int page,
      @RequestParam(defaultValue = "10", required = false) int size,
      @RequestParam(defaultValue = "DESC", required = false) Sort.Direction sortDirection,
      @RequestParam(defaultValue = "UPDATED_AT", required = false) PageUtils.CommonSortBy sortBy,
      @RequestParam(required = false) String keyword
  ){
    Pageable pageable = PageUtils.pageable(page, size);
    Page<Message> messageList = messageService.searchMessage(pageable,sortDirection,sortBy,keyword);

    return ResponseEntity.ok(messageList.map(MessageResponseDto::new));
  }
}
