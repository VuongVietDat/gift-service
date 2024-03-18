package vn.com.atomi.loyalty.gift.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftClaimOutput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.enums.ErrorCode;
import vn.com.atomi.loyalty.gift.repository.GiftClaimRepository;
import vn.com.atomi.loyalty.gift.repository.GiftRepository;
import vn.com.atomi.loyalty.gift.service.CustomerGiftService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerGiftServiceImpl extends BaseService implements CustomerGiftService {
  private final GiftRepository giftRepository;
  private final GiftClaimRepository giftClaimRepository;

  @Override
  public List<GiftOutput> getInternalMyGift(Integer type, Pageable pageable) {
    return null;
  }

  @Override
  public GiftClaimOutput internalClaimsGift(ClaimGiftInput input) {
    // todo: check bank CIF        // check ví

    // check gift ID, price
    var gift =
        giftRepository
            .findByIdAndScores(input.getGiftId(), input.getPrice())
            .orElseThrow(() -> new BaseException(ErrorCode.GIFT_NOT_EXISTED));

    // todo check group customer, api core

    // todo check so diem hien tai cua customer, api core

    // todo collect money API

    // convert
    var giftClaim = modelMapper.convertToGiftClaim(input);
    // save claims gift by quantity
    var entity = giftClaimRepository.save(giftClaim);

    return modelMapper.convertToGiftClaimOutput(entity);
  }
}
