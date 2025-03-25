package common.event;


import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.catalina.User;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderEventDto extends EventBaseDto {

    UUID orderId;
    UUID provideCompanyId;
    UUID receiverCompanyId;
    UUID hubId;
    List<OrderProductEventDto> products;

    public CreateOrderEventDto(UUID orderId, UUID provideCompanyId, UUID receiverCompanyId) {
        this.orderId = orderId;
        this.provideCompanyId = provideCompanyId;
        this.receiverCompanyId = receiverCompanyId;
    }
}
