package vn.com.atomi.loyalty.gift.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.annotations.DateTimeValidator;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.gift.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.gift.dto.input.CategoryInput;
import vn.com.atomi.loyalty.gift.dto.output.CategoryOutput;
import vn.com.atomi.loyalty.gift.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.gift.dto.output.InternalCategoryOutput;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.Status;
import vn.com.atomi.loyalty.gift.service.CategoryService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CategoryController extends BaseController {

  private final CategoryService categoryService;

  @Operation(summary = "APi tạo mới danh mục (bản ghi chờ duyệt)")
  @PostMapping("/categories/approvals")
  public ResponseEntity<ResponseData<Void>> createCategory(
      @RequestBody CategoryInput categoryInput) {
    categoryService.createCategory(categoryInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api lấy danh sách duyệt danh mục")
  @GetMapping("/categories/approvals")
  public ResponseEntity<ResponseData<ResponsePage<CategoryOutput>>> getCategoryApprovals(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(
              description =
                  "Trạng thái phê duyệt:</br> WAITING: Chờ duyệt</br> ACCEPTED: Đồng ý</br> REJECTED: Từ chối</br> RECALL: Thu hồi")
          @RequestParam(required = false)
          ApprovalStatus approvalStatus,
      @Parameter(
              description =
                  "Loại phê duyệt: </br>CREATE: Phê duyệt tạo</br>UPDATE: Phê duyệt cập nhật</br>CANCEL: Phê duyệt hủy bỏ")
          @RequestParam(required = false)
          ApprovalType approvalType,
      @Parameter(description = "Thời gian duyệt từ ngày (dd/MM/yyyy)")
          @DateTimeValidator(required = false)
          @RequestParam(required = false)
          String startApprovedDate,
      @Parameter(description = "Thời gian duyệt đến ngày (dd/MM/yyyy)")
          @DateTimeValidator(required = false)
          @RequestParam(required = false)
          String endApprovedDate,
      @Parameter(description = "Tên danh mục") @RequestParam(required = false) String name,
      @Parameter(description = "Mã danh mục") @RequestParam(required = false) String code) {
    return ResponseUtils.success(
        categoryService.getCategoryApprovals(
            status,
            approvalStatus,
            approvalType,
            startApprovedDate,
            endApprovedDate,
            name,
            code,
            super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy chi tiết bản ghi duyệt danh mục theo id")
  @GetMapping("/categories/approvals/{id}")
  public ResponseEntity<ResponseData<CategoryOutput>> getCategoryApproval(
      @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
    return ResponseUtils.success(categoryService.getCategoryApproval(id));
  }

  @Operation(
      summary =
          "Api so sánh bản ghi duyệt cập nhật hiện tại với bản ghi đã được phê duyệt trước đó")
  @GetMapping("/categories/approvals/{id}/comparison")
  public ResponseEntity<ResponseData<List<ComparisonOutput>>> getRuleApprovalComparison(
      @Parameter(description = "ID bản ghi duyệt cập nhật") @PathVariable Long id) {
    return ResponseUtils.success(categoryService.geCategoryApprovalComparison(id));
  }

  @Operation(summary = "Api thu hồi yêu cầu chờ duyệt danh mục theo id")
  @PutMapping("/categories/approvals/{id}/recall")
  public ResponseEntity<ResponseData<Void>> recallCategoryApproval(
      @Parameter(description = "ID bản ghi chờ duyệt") @PathVariable Long id) {
    categoryService.recallCategoryApproval(id);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api lấy danh sách danh mục")
  @GetMapping("/categories")
  public ResponseEntity<ResponseData<ResponsePage<CategoryOutput>>> getCategories(
      @Parameter(description = "Số trang, bắt đầu từ 1", example = "1") @RequestParam
          Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200", example = "10") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Trạng thái:</br> ACTIVE: Hiệu lực</br> INACTIVE: Không hiệu lực")
          @RequestParam(required = false)
          Status status,
      @Parameter(description = "Tên danh mục") @RequestParam(required = false) String name,
      @Parameter(description = "Mã danh mục") @RequestParam(required = false) String code) {
    return ResponseUtils.success(
        categoryService.getCategories(status, name, code, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api lấy chi tiết bản ghi danh mục theo id")
  @GetMapping("/categories/{id}")
  public ResponseEntity<ResponseData<CategoryOutput>> getCategory(
      @Parameter(description = "ID bản ghi danh mục") @PathVariable Long id) {
    return ResponseUtils.success(categoryService.getCategory(id));
  }

  @Operation(
      summary =
          "Api cập nhật bản ghi danh mục theo id (tương đương với việc tạo mới bản ghi chờ duyệt từ 1 bản ghi đã có)")
  @PutMapping("/categories/{id}")
  public ResponseEntity<ResponseData<Void>> updateCategory(
      @Parameter(description = "ID bản ghi danh mục") @PathVariable Long id,
      @RequestBody CategoryInput categoryInput) {
    categoryService.updateCategory(id, categoryInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api phê duyệt danh mục")
  @PutMapping("/categories/approvals")
  public ResponseEntity<ResponseData<Void>> approveCategory(@RequestBody ApprovalInput input) {
    categoryService.approveCategory(input);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api (nội bộ) lấy tất cả danh mục hiệu lực")
  @PreAuthorize(Authority.ROLE_SYSTEM)
  @PostMapping("/internal/categories")
  public ResponseEntity<ResponseData<List<InternalCategoryOutput>>> getInternalCategories(
      @Parameter(
              description = "Chuỗi xác thực khi gọi api nội bộ",
              example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
          @RequestHeader(RequestConstant.SECURE_API_KEY)
          @SuppressWarnings("unused")
          String apiKey) {
    return ResponseUtils.success(categoryService.getInternalCategories());
  }
}
