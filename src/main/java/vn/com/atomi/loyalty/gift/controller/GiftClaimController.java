package vn.com.atomi.loyalty.gift.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.com.atomi.loyalty.base.data.BaseController;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.base.data.ResponseUtils;
import vn.com.atomi.loyalty.base.security.Authority;
import vn.com.atomi.loyalty.gift.dto.input.GiftClaimInput;
import vn.com.atomi.loyalty.gift.service.GiftClaimService;

@RestController
@RequiredArgsConstructor
public class GiftClaimController extends BaseController {

    private final GiftClaimService giftClaimService;

    @PreAuthorize(Authority.ROLE_SYSTEM)
    @Operation(summary = "APi gắn quà cho khách hàng")
    @PostMapping("/internal/giftclaim/create")
    public ResponseEntity<ResponseData<Void>> createGiftClaim(@RequestBody GiftClaimInput giftClaimInput) {
        giftClaimService.create(giftClaimInput);
        return ResponseUtils.success();
    }
}
