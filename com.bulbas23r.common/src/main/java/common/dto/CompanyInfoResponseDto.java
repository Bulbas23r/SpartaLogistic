package common.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoResponseDto {

    private String companyName;
    private String type;
    private UUID hubId;
    Long managerId;
}
