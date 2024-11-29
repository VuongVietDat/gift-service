package vn.com.atomi.loyalty.gift.service;

import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.gift.dto.input.GiftPartnerInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftPartnerOutput;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.util.List;

/**
 * @author nghiatd
 * @version 1.0
 */
public interface GiftPartnerService {

  void create(GiftPartnerInput categoryInput);

  ResponsePage<GiftPartnerOutput> getGiftPartners(Status status, String effectiveDate ,String name, Long partnerId ,String categoryCode,Long categoryId, Pageable pageable);

  void update(Long id, GiftPartnerInput input);

  GiftPartnerOutput get(Long id);

}
