package vn.com.atomi.loyalty.gift.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.gift.dto.input.GiftInput;
import vn.com.atomi.loyalty.gift.dto.output.*;
import vn.com.atomi.loyalty.gift.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface GiftService {

  void create(GiftInput categoryInput);

  ResponsePage<InternalGiftOutput> getsI(Status status, String name, String code, Pageable pageable);
  ResponsePage<GiftOutput> gets(Status status, String name, String code, Pageable pageable);
  GiftOutput get(Long id);

  InternalGiftOutput getI(Long id);

  void update(Long id, GiftInput input);

  ResponsePage<InternalGiftOutput> getInternalGift(Long categoryId, Pageable pageable);

  List<PreviewGiftOutput> getPartnerGift(String partnerCode);

  InternalGiftOutput getInternalGift(Long id);
}
