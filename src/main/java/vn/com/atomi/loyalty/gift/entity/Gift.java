package vn.com.atomi.loyalty.gift.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  public static final String GENERATOR = "GS_GIFT_ID_SEQ";

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR)
  @SequenceGenerator(name = GENERATOR, sequenceName = GENERATOR, allocationSize = 1)
  private Long id;

  @Column(name = "CATEGORY_ID")
  private Long categoryId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "CODE")
  private String code;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "SERVICE")
  private String service;

  @Column(name = "DISCOUNT_TYPE")
  private String discountType;

  @Column(name = "DISCOUNT_VALUE")
  private Long discountValue;

  @Column(name = "START_DATE")
  private LocalDate startDate;

  @Column(name = "END_DATE")
  private LocalDate endDate;

  @Column(name = "DISCOUNT_MAX")
  private Long discountMax;

  @Column(name = "GIFT_COUNT")
  private Long giftCount;

  @Column(name = "TOTAL_REMAINING")
  private Long totalRemaining;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "GUIDE")
  private String guide;

  @Column(name = "TERMS_OF_USE")
  private String termsOfUse;

  @Column(name = "SUPPORT")
  private String support;

  @Column(name = "IMAGES")
  private String images;

  @Column(name = "THUMBNAIL")
  private String thumbnail;

  @Column(name = "BILL_VALUE_MIN")
  private Long billValueMin;

  @Column(name = "BILL_VALUE_MAX")
  private Long billValueMax;

  @Column(name = "SALE_START_DATE")
  private LocalDateTime saleStartDate;

  @Column(name = "SALE_END_DATE")
  private LocalDateTime saleEndDate;

  @Column(name = "NEED_APPROVAL")
  private Boolean needApproval;

  @Column(name = "LIMIT_GIFT_PER_USER")
  private Long limitGiftPerUser;

  @Column(name = "PRICE")
  private Long price;
}
