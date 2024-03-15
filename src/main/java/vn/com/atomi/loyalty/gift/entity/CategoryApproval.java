package vn.com.atomi.loyalty.gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.CategoryType;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.time.LocalDate;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GS_CATEGORY_APPROVAL")
public class CategoryApproval extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gs_category_arv_id_seq")
  @SequenceGenerator(
      name = "gs_category_arv_id_seq",
      sequenceName = "gs_category_arv_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "category_id")
  private Long categoryId;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "type")
  private String type;

  @Column(name = "service")
  private String service;

  @Column(name = "discount_type")
  private String discountType;

  @Column(name = "discount_value")
  private String discountValue;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

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

  @Column(name = "scores")
  private Long scores;

  @Column(name = "apply_users")
  @NotNull
  private String applyUsers;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "order_no")
  private Long orderNo;

  @Column(name = "category_type")
  @Enumerated(EnumType.STRING)
  private CategoryType categoryType;

  @Column(name = "approval_status")
  @Enumerated(EnumType.STRING)
  private ApprovalStatus approvalStatus;

  @Column(name = "approval_type")
  @Enumerated(EnumType.STRING)
  private ApprovalType approvalType;

  @Column(name = "approval_comment")
  private String approvalComment;

  @Column(name = "approver")
  private String approver;
}
