package vn.com.atomi.loyalty.gift.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftClaimOutput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.dto.output.MyGiftOutput;
import vn.com.atomi.loyalty.gift.enums.VoucherStatus;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerGiftService {

  ResponsePage<GiftOutput> getInternalMyGift(
      Long customerId, String cifBank, String cifWallet, VoucherStatus type, Pageable pageable);

  GiftClaimOutput internalClaimsGift(ClaimGiftInput claimGiftInput);

  GiftClaimOutput internalCheckStatus(String refNo);
}
