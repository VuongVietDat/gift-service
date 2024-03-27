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
  public static final String GENERATOR = "gs_gift_claim_id_seq";

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR)
  @SequenceGenerator(name = GENERATOR, sequenceName = GENERATOR, allocationSize = 1)
  private Long id;

  @Column(name = "cif_bank")
  private String cifBank;

  @Column(name = "cif_wallet")
  private String cifWallet;

  @Column(name = "gift_id")
  private Long giftId;

  @Column(name = "quantity")
  private Long quantity;

  @Column(name = "ref_no")
  private String refNo;

  @Column(name = "customer_id")
  private Long customerId;
}
