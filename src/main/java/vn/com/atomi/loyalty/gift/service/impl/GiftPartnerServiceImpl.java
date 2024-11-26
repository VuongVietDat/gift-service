package vn.com.atomi.loyalty.gift.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.gift.dto.input.GiftPartnerInput;
import vn.com.atomi.loyalty.gift.dto.output.*;
import vn.com.atomi.loyalty.gift.enums.ErrorCode;
import vn.com.atomi.loyalty.gift.enums.Status;
import vn.com.atomi.loyalty.gift.repository.CategoryRepository;
import vn.com.atomi.loyalty.gift.repository.GiftPartnerRepository;
import vn.com.atomi.loyalty.gift.repository.redis.GiftCacheRepository;
import vn.com.atomi.loyalty.gift.service.GiftPartnerService;
import vn.com.atomi.loyalty.gift.utils.Utils;

@Service
@RequiredArgsConstructor
public class GiftPartnerServiceImpl extends BaseService implements GiftPartnerService {
  private final CategoryRepository categoryRepository;
  private final GiftPartnerRepository giftPartnerRepository;
  private final GiftCacheRepository giftCacheRepository;

  @Override
  public void create(GiftPartnerInput input) {
    var startDate = Utils.convertToLocalDate(input.getStartDate());
    var endDate = Utils.convertToLocalDate(input.getEndDate());
    var effectiveDate = Utils.convertToLocalDate(input.getEffectiveDate());
    var expiredDate = Utils.convertToLocalDate(input.getExpiredDate());
    // check category
    categoryRepository
            .findByDeletedFalseAndIdAndStatus(input.getCategoryId(), Status.ACTIVE)
            .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_EXISTED));
    // lưu bản ghi
    var giftPartner = modelMapper.convertToGiftPartner(input, startDate, endDate, effectiveDate, expiredDate);
    giftPartnerRepository.save(giftPartner);
    // clear cache
    giftCacheRepository.clear();
  }

  @Override
  public ResponsePage<GiftPartnerOutput> getGiftPartners(Status status, String name,String categorycode,Long categoryId, Pageable pageable) {
    var page = giftPartnerRepository.findByCondition(name, status,categorycode, categoryId, pageable);
    var giftPartnerOutputs = modelMapper.convertToGiftPartnerOutputs(page.getContent());
    return new ResponsePage<>(page, giftPartnerOutputs);
  }

  @Override
  public void update(Long id, GiftPartnerInput input) {
    // lấy record hiện tại
    var record =
            giftPartnerRepository
                    .findByDeletedFalseAndId(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.GIFT_NOT_EXISTED));
    // check category
    categoryRepository
            .findByDeletedFalseAndIdAndStatus(input.getCategoryId(), Status.ACTIVE)
            .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_EXISTED));
    // mapping new values
    var newGiftPartner = super.modelMapper.mappingToGiftPartner(record, input);
    // lưu
    giftPartnerRepository.save(newGiftPartner);
    // clear cache
    giftCacheRepository.clear();
  }

  @Override
  public GiftPartnerOutput get(Long id) {
    var giftPartner =
            giftPartnerRepository
                    .findByDeletedFalseAndId(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.GIFT_NOT_EXISTED));
    var out =  super.modelMapper.convertToGiftPartnerOutput(giftPartner);
    return out;
  }
}