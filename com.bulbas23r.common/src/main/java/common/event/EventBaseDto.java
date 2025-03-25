package common.event;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EventBaseDto {
  private String username;
  private String role;
  private String authorization;
}
