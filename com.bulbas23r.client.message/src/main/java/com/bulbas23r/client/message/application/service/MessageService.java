package com.bulbas23r.client.message.application.service;

import com.bulbas23r.client.message.domain.model.Message;
import com.bulbas23r.client.message.presentation.dto.request.PostMessageDto;
import com.bulbas23r.client.message.presentation.dto.response.MessageResponseDto;
import common.utils.PageUtils.CommonSortBy;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public interface MessageService {

  void sendDirectMessage(PostMessageDto postMessageDto);

  void updateMessage(UUID messageId, String message);

  void deleteMessage(UUID messageId);

  MessageResponseDto getMessage(UUID messageId);

  Page<MessageResponseDto> getMessageList(Pageable pageable);

  Page<Message> searchMessage(Pageable pageable, Direction sortDirection, CommonSortBy sortBy, String keyword);
}
