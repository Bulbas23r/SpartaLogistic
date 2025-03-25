package common.event;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// TODO 삭제 예정
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStockEventDto {

    UUID hubId;
    List<OrderProductEventDto> products;
}
