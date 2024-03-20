package vn.com.atomi.loyalty.gift.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.gift.dto.input.GiftInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.entity.CategoryApproval;
import vn.com.atomi.loyalty.gift.enums.ErrorCode;
import vn.com.atomi.loyalty.gift.enums.Status;
import vn.com.atomi.loyalty.gift.repository.CategoryRepository;
import vn.com.atomi.loyalty.gift.repository.GiftRepository;
import vn.com.atomi.loyalty.gift.repository.redis.GiftCacheRepository;
import vn.com.atomi.loyalty.gift.service.GiftService;
import vn.com.atomi.loyalty.gift.utils.Utils;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl extends BaseService implements GiftService {
  private final CategoryRepository categoryRepository;
  private final GiftRepository giftRepository;
  private final GiftCacheRepository giftCacheRepository;

  @Override
  public void create(GiftInput input) {
    var startDate = Utils.convertToLocalDate(input.getStartDate());
    var endDate = Utils.convertToLocalDate(input.getEndDate());

    // check category
    categoryRepository
        .findByDeletedFalseAndIdAndStatus(input.getCategoryId(), Status.ACTIVE)
        .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_EXISTED));

    // check user group
    // todo: core API

    // tạo code
    var id = giftRepository.getSequence();
    var code = Utils.generateCode(id, CategoryApproval.class.getSimpleName());

    // lưu bản ghi
    var gift = modelMapper.convertToGift(input, startDate, endDate, id, code);
    giftRepository.save(gift);

    // clear cache
    giftCacheRepository.clear();
  }

  @Override
  public ResponsePage<GiftOutput> gets(Status status, String name, String code, Pageable pageable) {
    var page = giftRepository.findByCondition(code, name, status, pageable);
    var giftOutputs = modelMapper.convertToGiftOutputs(page.getContent());
    return new ResponsePage<>(page, giftOutputs);
  }

  @Override
  public GiftOutput get(Long id) {
    var gift =
        giftRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.GIFT_NOT_EXISTED));
    return super.modelMapper.convertToGiftOutput(gift);
  }

  @Override
  public void update(Long id, GiftInput input) {
    // lấy record hiện tại
    var record =
        giftRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.GIFT_NOT_EXISTED));

    // check category
    categoryRepository
        .findByDeletedFalseAndIdAndStatus(input.getCategoryId(), Status.ACTIVE)
        .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_EXISTED));

    // check user group
    // todo: core API

    // mapping new values
    var newCampaign = super.modelMapper.mappingToGift(record, input);
    // lưu
    giftRepository.save(newCampaign);

    // clear cache
    giftCacheRepository.clear();
  }

  @Override
  public ResponsePage<GiftOutput> getInternal(Long categoryId, Pageable pageable) {
    // load cache
    var cache = giftCacheRepository.gets(categoryId);
    if (cache.isPresent()) return cache.get();

    // load DB
    var page =
        categoryId == null
            ? giftRepository.findAllBy(pageable)
            : giftRepository.findAllByCategoryId(categoryId, pageable);

    var outputs = modelMapper.convertToGiftOutputs(page.getContent());

    var outputPage = new ResponsePage<>(page, outputs);

    // save cache
    if (!outputs.isEmpty()) giftCacheRepository.put(categoryId, outputPage);

    return outputPage;
  }
}
