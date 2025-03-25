package common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubInfoResponseDto {

    private String name;
    private Long managerId;
    private String roadAddress;
    private String jibunAddress;
    private Double latitude;
    private Double longitude;
    private Boolean active;
}
