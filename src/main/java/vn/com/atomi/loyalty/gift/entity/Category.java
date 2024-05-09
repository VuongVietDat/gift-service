package vn.com.atomi.loyalty.gift.entity;

import jakarta.persistence.*;
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
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GS_CATEGORY_ID_SEQ")
  @SequenceGenerator(
      name = "GS_CATEGORY_ID_SEQ",
      sequenceName = "GS_CATEGORY_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "CODE")
  private String code;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "ICON")
  private String appIcon;

  @Column(name = "ORDER_NO")
  private Long orderNo;

  @Column(name = "CATEGORY_TYPE")
  @Enumerated(EnumType.STRING)
  private CategoryType categoryType;

  @Column(name = "CREATOR")
  private String creator;

  @Column(name = "CREATION_DATE")
  private LocalDateTime creationDate;

  @Column(name = "CREATION_APPROVAL_DATE")
  private LocalDateTime creationApprovalDate;
}
