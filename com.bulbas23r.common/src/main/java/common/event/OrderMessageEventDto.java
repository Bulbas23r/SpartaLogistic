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
public class OrderMessageEventDto {

  private UUID orderId;
  // 주문 요청 사항
  private String memo;
  private List<ProductInfo> products;

  @Getter
  @Setter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ProductInfo {
    private String productName;
    private int quantity;
  }
}


