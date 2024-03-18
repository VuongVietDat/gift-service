package vn.com.atomi.loyalty.gift.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GiftClaimOutput {
  @Schema(description = "ID nhận quà")
  private Long id;

  @Schema(description = "Mã định danh của khách hàng trên bank")
  @NotBlank
  private String cifBank;

  @Schema(description = "Mã định danh của khách hàng trên ví")
  @NotBlank
  private String cifWallet;

  @Schema(description = "ID của quà")
  @NotNull
  private Long giftId;

  @Schema(description = "Số lượng")
  @NotNull
  @Min(1)
  private Long quantity;

  @Schema(description = "Số tham chiếu")
  @NotBlank
  private String refNo;
}
