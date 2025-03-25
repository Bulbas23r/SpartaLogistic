package common.event;


import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderEventDto {
    UUID orderId;
    UUID hubId;
    List<OrderProductEventDto> products;
}
