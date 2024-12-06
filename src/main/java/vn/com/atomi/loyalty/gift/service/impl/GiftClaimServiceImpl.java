package vn.com.atomi.loyalty.gift.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.gift.dto.input.GiftClaimInput;
import vn.com.atomi.loyalty.gift.repository.GiftClaimRepository;
import vn.com.atomi.loyalty.gift.service.GiftClaimService;

@Service
@RequiredArgsConstructor
public class GiftClaimServiceImpl extends BaseService implements GiftClaimService {

    private final GiftClaimRepository giftClaimRepository;

    @Override
    public void create(GiftClaimInput giftClaimInput) {

        // lưu bản ghi
        var giftClaim = modelMapper.convertToGiftGiftClaim(giftClaimInput);
        giftClaimRepository.save(giftClaim);
    }

}
