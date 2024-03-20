package vn.com.atomi.loyalty.gift.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.data.*;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftClaimOutput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.service.CustomerGiftService;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CustomerGiftController extends BaseController {
  private final CustomerGiftService customerGiftService;

  @Operation(summary = "Api (nội bộ) lấy danh sách quà của tôi")
  @PreAuthorize(Authority.ROLE_SYSTEM)
  @GetMapping("/internal/my-gifts")
  public ResponseEntity<ResponseData<ResponsePage<GiftOutput>>> getInternalMyGift(
      @Parameter(description = "Số trang, bắt đầu từ 1") @RequestParam Integer pageNo,
      @Parameter(description = "Số lượng bản ghi 1 trang, tối đa 200") @RequestParam
          Integer pageSize,
      @Parameter(description = "Sắp xếp, Pattern: ^[a-z0-9]+:(asc|desc)")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "ID của khách hàng") Long customerId,
      @Parameter(
              description =
                  "Các điều kiện lọc: </br>1: Chưa dùng</br>2: Đã dùng</br>3: Đã dùng point để claims gift")
          @RequestParam(required = false, defaultValue = "1")
          Integer type) {
    return ResponseUtils.success(
        customerGiftService.getInternalMyGift(
            customerId, type, super.pageable(pageNo, pageSize, sort)));
  }

  @Operation(summary = "Api (nội bộ) dùng point để claims gift")
  @PreAuthorize(Authority.ROLE_SYSTEM)
  @PostMapping("/internal/claim-gifts")
  public ResponseEntity<ResponseData<GiftClaimOutput>> internalClaimsGift(
      @RequestBody ClaimGiftInput claimGiftInput) {
    return ResponseUtils.success(customerGiftService.internalClaimsGift(claimGiftInput));
  }
}
