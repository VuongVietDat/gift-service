package vn.com.atomi.loyalty.gift.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.time.LocalDate;
import java.util.Date;

@Data
public class GiftPartnerInput {
  @Schema(description = "Id đối tác")
  @NotBlank
  @Size(max = 10)
  private Long partnerId;

  @Schema(description = "Id danh mục")
  @NotBlank
  private Long categoryId;

  @Schema(description = "Tên quà")
  @NotBlank
  private String name;

  @Schema(description = "Nhóm quà")
  @NotBlank
  private String giftType;

  @Schema(description = "Số lượng")
  @NotBlank
  private Long qtyInit;

  @Schema(description = "Số lượng còn lại")
  @NotBlank
  private Long qtyRemain;

  @Schema(description = "Điểm đổi quà")
  @NotBlank
  private Long price;

  @Schema(description = "Đơn vị tính")
  @NotBlank
  private String unit;

  @Schema(description = "Ngày bắt đầu mở bán (dd/MM/yyyy)", example = "01/01/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String startDate;

  @Schema(description = "Ngày kết thúc mở bán(dd/MM/yyyy)", example = "31/12/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String endDate;

  @Schema(description = "Ngày hiệu lực(dd/MM/yyyy)", example = "15/05/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String effectiveDate;

  @Schema(description = "Ngày hết hiệu lực(dd/MM/yyyy)", example = "31/12/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String expiredDate;

  @Schema(description = "Trạng thái ")
  @NotBlank
  private Status status;

  @Schema(description = "Hiển thị trên app")
  @NotBlank
  private String visible;

  @Schema(description = "Mô tả quà")
  @NotBlank
  private String notes;

  @Schema(description = "Hướng dẫn sử dụng")
  @NotBlank
  private String guide;

  @Schema(description = "Điều kiện áp dụng")
  @NotBlank
  private String condition;

  @Schema(description = "Ảnh voucher")
  @NotBlank
  private String image;

  @Schema(description = "Ảnh thumbnail")
  @NotBlank
  private String thumbnail;

}
