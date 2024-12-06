package vn.com.atomi.loyalty.gift.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import vn.com.atomi.loyalty.gift.enums.VoucherStatus;

import java.util.Date;

@Data
public class GiftClaimInput {

    @Schema(description = "ID khách hàng")
    private Long customerId;

    @Schema(description = "ID quà")
    private Long giftId;

    @Schema(description = "Giá quà")
    private Long prince;

    @Schema(description = "Số lượng đổi")
    private Long quantity;

    @Schema(description = "Tổng điểm đổi")
    private Long totalPoint;

    @Column(name = "DATE_CLAIM")
    private Date claimsAt;

    @Column(name = "END_DATE")
    private Date endDate;

    @Schema(description = "AVAILABLE: Chưa dùng</br>USED: Đã dùng</br>EXPIRED: Hết hạn</br>CLAIMED: Đã dùng point để claims gift")
    @Enumerated(EnumType.STRING)
    private VoucherStatus voucherStatus;

    @Schema(description = "Số tham chiếu")
    private String refNo;
}
