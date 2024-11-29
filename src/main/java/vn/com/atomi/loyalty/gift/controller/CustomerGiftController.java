package vn.com.atomi.loyalty.gift.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftClaimOutput;
import vn.com.atomi.loyalty.gift.dto.output.MyGiftOutput;
import vn.com.atomi.loyalty.gift.enums.VoucherStatus;
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
    public ResponseEntity<ResponseData<ResponsePage<MyGiftOutput>>> getInternalMyGift(
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

            @Parameter(description = "ID của khách hàng")
            @RequestParam(required = true) Long customerId,

            @Parameter(description = "Các điều kiện lọc: </br>AVAILABLE: Chưa dùng</br>USED: Đã dùng</br>EXPIRED: Hết hạn</br>CLAIMED: Đã dùng point để claims gift")
            @RequestParam(required = false)
            VoucherStatus type) {

        return ResponseUtils.success(customerGiftService.getInternalMyGift(customerId, type, super.pageable(pageNo, pageSize, sort)));
    }

    @Operation(summary = "Api (nội bộ) dùng point để claims gift")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @PostMapping("/internal/claim-gifts")
    public ResponseEntity<ResponseData<GiftClaimOutput>> internalClaimsGift(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @RequestBody ClaimGiftInput claimGiftInput) {
        return ResponseUtils.success(customerGiftService.internalClaimsGift(claimGiftInput));
    }

    @Operation(summary = "Api (nội bộ) kiểm tra trạng thái giao dịch đổi quà")
    @PreAuthorize(Authority.ROLE_SYSTEM)
    @GetMapping("/internal/transaction-status")
    public ResponseEntity<ResponseData<GiftClaimOutput>> internalCheckStatus(
            @Parameter(
                    description = "Chuỗi xác thực khi gọi api nội bộ",
                    example = "eb6b9f6fb84a45d9c9b2ac5b2c5bac4f36606b13abcb9e2de01fa4f066968cd0")
            @RequestHeader(RequestConstant.SECURE_API_KEY)
            @SuppressWarnings("unused")
            String apiKey,
            @Parameter(description = "Số tham chiếu") @RequestParam String refNo) {
        return ResponseUtils.success(customerGiftService.internalCheckStatus(refNo));
    }
}
