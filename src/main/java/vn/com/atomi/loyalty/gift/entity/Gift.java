package vn.com.atomi.loyalty.gift.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.gift.enums.Status;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GS_GIFT")
public class Gift extends BaseEntity {
  public static final String GENERATOR = "gs_gift_id_seq";

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR)
  @SequenceGenerator(name = GENERATOR, sequenceName = GENERATOR, allocationSize = 1)
  private Long id;

  @Column(name = "category_id")
  private Long categoryId;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "type")
  private String type;

  @Column(name = "service")
  private String service;

  @Column(name = "discount_type")
  private String discountType;

  @Column(name = "discount_value")
  private String discountValue;

  @Column(name = "start_date")
  private String startDate;

  @Column(name = "end_date")
  private String endDate;

  @Column(name = "discount_max")
  private Long discountMax;

  @Column(name = "gift_count")
  private Long giftCount;

  @Column(name = "description")
  private String description;

  @Column(name = "bill_value_min")
  private Long billValueMin;

  @Column(name = "bill_value_max")
  private Long billValueMax;

  @Column(name = "sale_start_date")
  private String saleStartDate;

  @Column(name = "sale_end_date")
  private String saleEndDate;

  @Column(name = "rank")
  private String rank;

  @Column(name = "need_approval")
  private Boolean needApproval;

  @Column(name = "limit_gift_per_user")
  private Long limitGiftPerUser;

  @Column(name = "price")
  private Long price;

  @Column(name = "customer_group_id")
  private Long customerGroupId;
}
