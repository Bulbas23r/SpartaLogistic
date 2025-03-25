package common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import common.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
    @JsonProperty("userId")
    private Long userId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("slackId")
    private String slackId;
    @JsonProperty("role")
    private UserRoleEnum role;
}
