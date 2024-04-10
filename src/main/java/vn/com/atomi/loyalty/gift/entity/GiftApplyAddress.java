package vn.com.atomi.loyalty.gift.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GS_GIFT_APPLY_ADDRESS")
public class GiftApplyAddress extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GS_GIFT_APPLY_ADD_ID_SEQ")
  @SequenceGenerator(
      name = "GS_GIFT_APPLY_ADD_ID_SEQ",
      sequenceName = "GS_GIFT_APPLY_ADD_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "GIFT_ID")
  private Long giftId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ADDRESS")
  private String address;

  @Column(name = "LONGITUDE")
  private String longitude;

  @Column(name = "LATITUDE")
  private String latitude;
}
