package common.event;


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
public class CreateStockEventDto {

    UUID hubId;
    UUID productId;
    int quantity;
}
