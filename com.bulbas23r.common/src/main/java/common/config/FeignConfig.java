package common.config;

import common.filter.FeignClientHeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Bean
  public FeignClientHeaderInterceptor feignInterceptor(){
    return new FeignClientHeaderInterceptor();
  }

}
