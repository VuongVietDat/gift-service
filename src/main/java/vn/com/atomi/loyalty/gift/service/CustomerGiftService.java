package vn.com.atomi.loyalty.gift.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;

/**
 * @author haidv
 * @version 1.0
 */
public interface CustomerGiftService {

  List<GiftOutput> getInternalMyGift(Integer type, Pageable pageable);

  List<GiftOutput> internalClaimsGift(ClaimGiftInput claimGiftInput);
}
