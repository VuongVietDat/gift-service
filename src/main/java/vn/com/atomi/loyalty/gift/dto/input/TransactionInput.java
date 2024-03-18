package vn.com.atomi.loyalty.gift.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter
@Getter
public class TransactionInput {

  @Schema(description = "ID khách hàng bên loyalty")
  @NotNull
  private Long customerId;

  @Schema(description = "Số tham chiếu")
  @NotBlank
  private String refNo;

  @Schema(description = "Số point thay đổi")
  @NotNull
  private Long amount;
}
