package vn.com.atomi.loyalty.gift.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.gift.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.gift.dto.input.CategoryInput;
import vn.com.atomi.loyalty.gift.dto.input.GiftInput;
import vn.com.atomi.loyalty.gift.dto.output.CategoryOutput;
import vn.com.atomi.loyalty.gift.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.dto.output.InternalCategoryOutput;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface GiftService {

  void create(GiftInput categoryInput);

  ResponsePage<GiftOutput> gets(Status status, String name, String code, Pageable pageable);

  GiftOutput get(Long id);

  void update(Long id, GiftInput input);

  ResponsePage<GiftOutput> getInternal(Long categoryId, Pageable pageable);
}
