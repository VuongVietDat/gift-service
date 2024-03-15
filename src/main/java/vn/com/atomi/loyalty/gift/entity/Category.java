package vn.com.atomi.loyalty.gift.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.gift.enums.CategoryType;
import vn.com.atomi.loyalty.gift.enums.Status;

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
@Table(name = "GS_CATEGORY")
public class Category extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gs_category_id_seq")
  @SequenceGenerator(
      name = "gs_category_id_seq",
      sequenceName = "gs_category_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "order_no")
  private Long orderNo;

  @Column(name = "category_type")
  @Enumerated(EnumType.STRING)
  private CategoryType categoryType;

  @Column(name = "creator")
  private String creator;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @Column(name = "creation_approval_date")
  private LocalDateTime creationApprovalDate;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;
}
