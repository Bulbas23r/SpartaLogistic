package common.config;

import common.feign.CustomErrorDecoder;
import common.filter.FeignClientHeaderInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Bean
  public FeignClientHeaderInterceptor feignInterceptor(){
    return new FeignClientHeaderInterceptor();
  }

  @Bean
  public ErrorDecoder errorDecoder(){
    return new CustomErrorDecoder();
  }
}
