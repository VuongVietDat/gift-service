package vn.com.atomi.loyalty.gift.dto.output;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GiftApplyAddressOutput {

  private Long id;

  private Long giftId;

  private String name;

  private String address;

  private String longitude;

  private String latitude;
}
