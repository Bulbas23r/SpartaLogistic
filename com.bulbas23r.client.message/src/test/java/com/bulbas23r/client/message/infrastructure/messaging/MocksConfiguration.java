package com.bulbas23r.client.message.infrastructure.messaging;

import com.bulbas23r.client.message.application.service.gemini.GeminiService;
import com.bulbas23r.client.message.application.service.message.MessageService;
import com.bulbas23r.client.message.client.DeliveryClient;
import com.bulbas23r.client.message.client.HubClient;
import com.bulbas23r.client.message.client.UserClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class MocksConfiguration {
  @Bean
  @Primary
  public DeliveryClient deliveryClient() {
    return Mockito.mock(DeliveryClient.class);
  }

  @Bean
  @Primary
  public HubClient hubClient() {
    return Mockito.mock(HubClient.class);
  }

  @Bean
  @Primary
  public UserClient userClient() {
    return Mockito.mock(UserClient.class);
  }

  @Bean
  @Primary
  public GeminiService geminiService() {
    return Mockito.mock(GeminiService.class);
  }

  @Bean
  @Primary
  public MessageService messageService() {
    return Mockito.mock(MessageService.class);
  }
}
