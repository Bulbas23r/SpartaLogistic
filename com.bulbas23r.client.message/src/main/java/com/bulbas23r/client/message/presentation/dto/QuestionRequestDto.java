package com.bulbas23r.client.message.presentation.dto;

import common.event.OrderMessageEventDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequestDto {
  private OrderMessageEventDto orderMessageEventDto;
  private String departureHubName;
  private List<String> transitHubNames = new ArrayList<>();
  private String arriveHubName;
}
