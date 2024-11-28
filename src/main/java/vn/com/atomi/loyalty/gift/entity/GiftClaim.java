package vn.com.atomi.loyalty.gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.gift.enums.VoucherStatus;

import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GS_GIFT_CLAIM")
public class GiftClaim extends BaseEntity {

    @Schema(description = "ID")
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GS_GIFT_CLAIM_ID_SEQ")
    @SequenceGenerator(
            name = "GS_GIFT_CLAIM_ID_SEQ",
            sequenceName = "GS_GIFT_CLAIM_ID_SEQ",
            allocationSize = 1)
    private Long id;

    @Schema(description = "ID khách hàng")
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Schema(description = "ID quà")
    @Column(name = "GIFT_ID")
    private Long giftId;

    @Schema(description = "Giá quà")
    @Column(name = "PRICE")
    private Long prince;

    @Schema(description = "Số lượng đổi")
    @Column(name = "QUANTITY")
    private Long quantity;

    @Schema(description = "Tổng điểm đổi")
    @Column(name = "TOTAL_POINT")
    private Long totalPoint;

    @Column(name = "DATE_CLAIM")
    private Date claimsAt;

    @Column(name = "END_DATE")
    private Date endDate;

    @Schema(description = "AVAILABLE: Chưa dùng</br>USED: Đã dùng</br>EXPIRED: Hết hạn</br>CLAIMED: Đã dùng point để claims gift")
    @Enumerated(EnumType.STRING)
    private VoucherStatus voucherStatus;

    @Column(name = "REF_NO")
    private String refNo;

}
