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
import vn.com.atomi.loyalty.gift.dto.input.GiftInput;
import vn.com.atomi.loyalty.gift.dto.input.GiftPartnerInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.dto.output.GiftPartnerOutput;
import vn.com.atomi.loyalty.gift.dto.output.InternalGiftOutput;
import vn.com.atomi.loyalty.gift.dto.output.PreviewGiftOutput;
import vn.com.atomi.loyalty.gift.enums.Status;
import vn.com.atomi.loyalty.gift.service.GiftPartnerService;
import vn.com.atomi.loyalty.gift.service.GiftService;

import java.util.List;

import static vn.com.atomi.loyalty.base.security.Authority.Gift.*;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class GiftPartnerController extends BaseController {
  private final GiftPartnerService giftPartnerService;

  @PreAuthorize(CREATE_GIFT)
  @Operation(summary = "APi tạo mới quà doi tac")
  @PostMapping("/gift_partner")
  public ResponseEntity<ResponseData<Void>> createGiftPartner(@RequestBody GiftPartnerInput categoryInput) {
    giftPartnerService.create(categoryInput);
    return ResponseUtils.success();
  }

  @Operation(summary = "Api lấy danh sách quà")
  @GetMapping("/gift_partners")
  public ResponseEntity<ResponseData<ResponsePage<GiftPartnerOutput>>> getGiftPartners(
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
          @Parameter(description = "Tên quà")
          @RequestParam(required = false)
          String name,
          @Parameter(description = "Id danh mục")
          @RequestParam(required = false)
          Long categoryId) {
    return ResponseUtils.success(
            giftPartnerService.getGiftPartners(status, name, categoryId, super.pageable(pageNo, pageSize, sort)));
  }


    @PreAuthorize(UPDATE_GIFT)
    @Operation(summary = "Api cập nhật bản ghi quà theo id")
    @PutMapping("/gift_partners/{id}")
    public ResponseEntity<ResponseData<Void>> updateGiftPartner(
            @Parameter(description = "ID bản ghi quà") @PathVariable Long id,
            @RequestBody GiftPartnerInput categoryInput) {
        giftPartnerService.update(id, categoryInput);
        return ResponseUtils.success();
    }

    @Operation(summary = "Api lấy chi tiết bản ghi quà theo id")
    @GetMapping("/gift_partners/{id}")
    public ResponseEntity<ResponseData<GiftPartnerOutput>> get(
            @Parameter(description = "ID bản ghi quà") @PathVariable Long id) {
        return ResponseUtils.success(giftPartnerService.get(id));
    }
}
