package vn.com.atomi.loyalty.gift.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.input.TransactionInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftClaimOutput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;
import vn.com.atomi.loyalty.gift.enums.ErrorCode;
import vn.com.atomi.loyalty.gift.enums.GiftStatus;
import vn.com.atomi.loyalty.gift.feign.LoyaltyCoreClient;
import vn.com.atomi.loyalty.gift.repository.GiftClaimRepository;
import vn.com.atomi.loyalty.gift.repository.GiftRepository;
import vn.com.atomi.loyalty.gift.service.CustomerGiftService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomerGiftServiceImpl extends BaseService implements CustomerGiftService {
  private final GiftRepository giftRepository;
  private final GiftClaimRepository giftClaimRepository;
  private final LoyaltyCoreClient coreClient;

  @Override
  public ResponsePage<GiftOutput> getInternalMyGift(
      Long customerId, GiftStatus type, Pageable pageable) {
    // todo chua co API dung qua

    // lấy quà
    if (type == GiftStatus.CLAIMED) {
      var page = giftClaimRepository.findByCustomerId(customerId, pageable);

      return new ResponsePage<>(
          page,
          CollectionUtils.isEmpty(page.getContent())
              ? new ArrayList<>()
              : modelMapper.toGiftOutputs(page.getContent()));
    }
    var page = giftRepository.findAllBy(pageable);
    return new ResponsePage<>(
        page,
        CollectionUtils.isEmpty(page.getContent())
            ? new ArrayList<>()
            : modelMapper.convertToGiftOutputs(page.getContent()));
  }

  @Override
  public GiftClaimOutput internalClaimsGift(ClaimGiftInput input) {
    var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    var requestId = requestAttributes.getRequest().getHeader(RequestConstant.REQUEST_ID);

    // check bank CIF - check ví
    var res = coreClient.getCurrentBalance(requestId, input.getCifBank(), input.getCifWallet());
    if (res.getCode() != 0) throw new BaseException(ErrorCode.CIF_NOT_EXISTED);
    var cus = res.getData();

    // check so diem hien tai cua customer
    var fee = input.getQuantity() * input.getPrice();
    if (fee > cus.getAvailableAmount()) throw new BaseException(ErrorCode.AMOUNT_NOT_ENOUGH);

    // check gift ID, price
    if (!giftRepository.existsByIdAndPrice(input.getGiftId(), input.getPrice()))
      throw new BaseException(ErrorCode.GIFT_NOT_EXISTED);

    // collect money API
    var resTrans =
        coreClient.executeTransactionMinus(
            requestId, new TransactionInput(cus.getCustomerId(), input.getRefNo(), fee));
    if (resTrans.getCode() != 0)
      throw new BaseException(
          new String[] {resTrans.getService(), resTrans.getMessage()}, ErrorCode.TRANS_ERROR);

    // save claims gift by quantity
    var giftClaim = modelMapper.convertToGiftClaim(input, input.getGiftId(), cus.getCustomerId());
    var entity = giftClaimRepository.save(giftClaim);

    return modelMapper.convertToGiftClaimOutput(entity);
  }
}
