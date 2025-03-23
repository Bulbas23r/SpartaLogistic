package com.bulbas23r.client.message.application.service.gemini;

import com.bulbas23r.client.message.presentation.dto.request.GeminiRequestDto;
import com.bulbas23r.client.message.presentation.dto.response.GeminiResponseDto;
import common.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeminiServiceImpl implements GeminiService{

  private final RestTemplate restTemplate;

  private final static String AI_URI =  "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyAbaqOzv_9p4YTvi0RUF-arvfuAYZyHQNI";


  @Override
  public String getAi(String question) {
    GeminiRequestDto requestDto = new GeminiRequestDto(question);
    GeminiResponseDto responseDto = restTemplate.postForObject(AI_URI, requestDto, GeminiResponseDto.class);
    String message = responseDto.getCandidates().get(0).getContent().getParts().get(0).getText().toString();
    return message;
  }
}
