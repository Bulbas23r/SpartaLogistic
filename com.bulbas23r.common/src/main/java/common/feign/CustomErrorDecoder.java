package common.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.exception.NotFoundException;
import common.exception.UnauthorizedException;
import common.template.ApiResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
  private final ErrorDecoder defaultErrorDecoder = new Default();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Exception decode(String methodKey, Response response) {
    // 예시로 404와 401 상태 처리
    if (response.status() == HttpStatus.NOT_FOUND.value()) {
      String errorMessage = extractErrorMessage(response);
      return new NotFoundException(errorMessage);
    }
    if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
      String errorMessage = extractErrorMessage(response);
      return new UnauthorizedException(errorMessage);
    }
    return defaultErrorDecoder.decode(methodKey, response);
  }

  private String extractErrorMessage(Response response) {
    if (response.body() != null) {
      try (InputStream is = response.body().asInputStream()) {
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        // ApiResponse 객체로 파싱 시도
        try {
          ApiResponse<?> errorResponse = objectMapper.readValue(body, new TypeReference<ApiResponse<?>>() {});
          if (errorResponse != null && errorResponse.getMessage() != null) {
            return errorResponse.getMessage();
          }
        } catch (Exception ex) {
          // JSON 파싱 실패 시, body 자체를 반환
          return body;
        }
      } catch (IOException e) {
        // 예외 발생 시 상태코드를 포함한 기본 메시지 반환
        return "Error occurred with status code " + response.status();
      }
    }
    return "Error occurred with status code " + response.status();
  }
}
