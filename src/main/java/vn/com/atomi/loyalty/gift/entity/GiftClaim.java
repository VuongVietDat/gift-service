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
@Table(name = "GS_GIFT_CLAIM")
public class GiftClaim extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GS_GIFT_CLAIM_ID_SEQ")
  @SequenceGenerator(
      name = "GS_GIFT_CLAIM_ID_SEQ",
      sequenceName = "GS_GIFT_CLAIM_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "CIF_BANK")
  private String cifBank;

  @Column(name = "CIF_WALLET")
  private String cifWallet;

  @Column(name = "GIFT_ID")
  private Long giftId;

  @Column(name = "QUANTITY")
  private Long quantity;

  @Column(name = "REF_NO")
  private String refNo;

  @Column(name = "CUSTOMER_ID")
  private Long customerId;
}
