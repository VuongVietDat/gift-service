package vn.com.atomi.loyalty.gift.dto.output;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class CategoryApprovalOutput {
  @Schema(description = "ID bản ghi")
  private Long id;

  @Schema(description = "Tên danh mục")
  @NotBlank
  @Size(max = 168)
  private String name;

  @Schema(description = "Loại danh mục", example = "Internal Coupon")
  @NotBlank
  private String type;

  @Schema(description = "Dịch vụ áp dụng", example = "Transfer")
  @NotBlank
  private String service;

  @Schema(description = "Loại giảm giá", example = "Percentage")
  @NotBlank
  private String discountType;

  @Schema(description = "Giá trị giảm")
  @NotBlank
  private String discountValue;

  @Schema(description = "Ngày bắt đầu hiệu lực (dd/MM/yyyy)", example = "01/01/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String startDate;

  @Schema(description = "Ngày kết thúc hiệu lực (dd/MM/yyyy)", example = "31/12/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE, required = false)
  private String endDate;

  @Schema(description = "Giảm tối đa")
  @NotBlank
  private Long discountMax;

  @Schema(description = "Tổng số quà")
  @NotBlank
  private Long giftCount;

  @Schema(description = "Mô tả")
  @NotBlank
  private String description;

  @Schema(description = "Giá trị đơn hàng tối thiểu")
  @NotBlank
  private Long billValueMin;

  @Schema(description = "Giá trị đơn hàng tối đa")
  @NotBlank
  private Long billValueMax;

  @Schema(description = "Ngày bắt đầu bán (dd/MM/yyyy)", example = "01/01/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String saleStartDate;

  @Schema(description = "Ngày kết thúc bán (dd/MM/yyyy)", example = "01/01/2024")
  @DateTimeValidator(pattern = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  private String saleEndDate;

  @NotBlank
  @Schema(description = "Hạng được mua", example = "Gold")
  private String rank;

  @Schema(description = "Cần phê duyệt quà")
  private Boolean needApproval;

  @Schema(description = "Giới hạn quà/ người dùng")
  private Long limitGiftPerUser;

  @Schema(description = "Số điểm đổi")
  private Long scores;

  @Schema(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
  @NotNull
  private Status status;

  @Schema(description = "Danh sách Người dùng được đổi", example = "CIF1, CIF2,...")
  @NotNull
  private List<String> applyUsernames;

  @Schema(description = "Người tạo")
  private String creator;

  @Schema(description = "Ngày tạo")
  private LocalDateTime creationDate;

  @Schema(
      description =
          "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
  private ApprovalStatus approvalStatus;

  @Schema(
      description =
          "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
  private ApprovalType approvalType;

  @Schema(description = "Lý do đồng ý hoặc từ chối")
  private String approvalComment;

  @Schema(description = "Người duyệt")
  private String approver;
}
