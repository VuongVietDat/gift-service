package vn.com.atomi.loyalty.gift.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
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

  @Size(max = 30)
  @Column(name = "CODE", length = 30)
  private String code;

  @Size(max = 255)
  @Nationalized
  @Column(name = "NAME", length = 200)
  private String name;

  @Size(max = 10)
  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "ORDER_NO")
  private Long orderNo;

  @Size(max = 100)
  @Nationalized
  @Column(name = "IMAGE", length = 100)
  private String image;

  @Size(max = 200)
  @Column(name = "ICON", length = 200)
  private String icon;

  @Size(max = 20)
  @Column(name = "TYPE", length = 20)
  @Enumerated(EnumType.STRING)
  private CategoryType type;
}
