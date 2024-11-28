package vn.com.atomi.loyalty.gift.controller;

import static vn.com.atomi.loyalty.base.security.Authority.Gift.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.gift.dto.input.GiftInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.dto.output.InternalGiftOutput;
import vn.com.atomi.loyalty.gift.dto.output.PreviewGiftOutput;
import vn.com.atomi.loyalty.gift.enums.Status;
import vn.com.atomi.loyalty.gift.service.GiftService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class GiftController extends BaseController {
  private final GiftService giftService;

  @PreAuthorize(CREATE_GIFT)
  @Operation(summary = "APi tạo mới quà")
  @PostMapping("/gifts")
  public ResponseEntity<ResponseData<Void>> createGift(@RequestBody GiftInput categoryInput) {
    giftService.create(categoryInput);
    return ResponseUtils.success();
  }

  @PreAuthorize(READ_GIFT)
  @Operation(summary = "Api lấy danh sách quà")
  @GetMapping("/gifts")
  public ResponseEntity<ResponseData<ResponsePage<GiftOutput>>> getGifts(
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
      @Parameter(description = "Tên quà") @RequestParam(required = false) String name,
      @Parameter(description = "Mã quà") @RequestParam(required = false) String code) {
    return ResponseUtils.success(
        giftService.gets(status, name, code, super.pageable(pageNo, pageSize, sort)));
  }

  @PreAuthorize(READ_GIFT)
  @Operation(summary = "Api lấy danh sách quà của đối tác")
  @GetMapping("/partner-gifts")
  public ResponseEntity<ResponseData<List<PreviewGiftOutput>>> getPartnerGift(
      @Parameter(description = "Mã đối tác", example = "C_TIME") @RequestParam(required = false)
          String partnerCode) {
    return ResponseUtils.success(giftService.getPartnerGift(partnerCode));
  }

  @PreAuthorize(READ_GIFT)
  @Operation(summary = "Api lấy chi tiết bản ghi quà theo id")
  @GetMapping("/gifts/{id}")
  public ResponseEntity<ResponseData<GiftOutput>> getGift(
      @Parameter(description = "ID bản ghi quà") @PathVariable Long id) {
    return ResponseUtils.success(giftService.get(id));
  }

  @PreAuthorize(UPDATE_GIFT)
  @Operation(summary = "Api cập nhật bản ghi quà theo id")
  @PutMapping("/gifts/{id}")
  public ResponseEntity<ResponseData<Void>> updateGift(
      @Parameter(description = "ID bản ghi quà") @PathVariable Long id,
      @RequestBody GiftInput categoryInput) {
    giftService.update(id, categoryInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api (nội bộ) lấy tất cả quà hiệu lực")
  @PreAuthorize(Authority.ROLE_SYSTEM)
  @GetMapping("/internal/gifts")
  public ResponseEntity<ResponseData<ResponsePage<InternalGiftOutput>>> getInternalGift(
      @Parameter(
              description = "Chuỗi xác thực khi gọi api nội bộ",
              example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
          @RequestHeader(RequestConstant.SECURE_API_KEY)
          @SuppressWarnings("unused")
          String apiKey,
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
      @Parameter(description = "Tên quà") @RequestParam(required = false) String name,
      @Parameter(description = "Mã quà") @RequestParam(required = false) String code,
      @Parameter(description = "Mã danh mục") @RequestParam(required = false) String category) {
    return ResponseUtils.success(
        giftService.getsI(status, name, code, category, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api (nội bộ) lấy chi tiết quà")
  @PreAuthorize(Authority.ROLE_SYSTEM)
  @GetMapping("/internal/gifts/{id}")
  public ResponseEntity<ResponseData<InternalGiftOutput>> getInternalGift(
      @Parameter(
              description = "Chuỗi xác thực khi gọi api nội bộ",
              example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
          @RequestHeader(RequestConstant.SECURE_API_KEY)
          @SuppressWarnings("unused")
          String apiKey,
      @PathVariable Long id) {
    return ResponseUtils.success(giftService.getI(id));
  }
}
