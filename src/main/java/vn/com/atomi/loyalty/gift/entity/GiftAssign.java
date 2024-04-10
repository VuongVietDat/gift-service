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
@Table(name = "GS_GIFT_ASSIGN")
public class GiftAssign extends BaseEntity {
  public static final String GENERATOR = "GS_GIFT_ASSIGN_ID_SEQ";

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR)
  @SequenceGenerator(name = GENERATOR, sequenceName = GENERATOR, allocationSize = 1)
  private Long id;

  @Column(name = "GIFT_ID")
  private Long giftId;

  @Column(name = "CUSTOMER_GROUP_ID")
  private String customerGroupId;
}
