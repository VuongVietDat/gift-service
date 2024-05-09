package vn.com.atomi.loyalty.gift.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.input.TransactionInput;
import vn.com.atomi.loyalty.gift.dto.output.GiftClaimOutput;
import vn.com.atomi.loyalty.gift.dto.output.MyGiftOutput;
import vn.com.atomi.loyalty.gift.enums.ErrorCode;
import vn.com.atomi.loyalty.gift.enums.VoucherStatus;
import vn.com.atomi.loyalty.gift.feign.LoyaltyCoreClient;
import vn.com.atomi.loyalty.gift.repository.GiftClaimRepository;
import vn.com.atomi.loyalty.gift.repository.GiftRepository;
import vn.com.atomi.loyalty.gift.service.CustomerGiftService;

@Service
@RequiredArgsConstructor
public class CustomerGiftServiceImpl extends BaseService implements CustomerGiftService {
  private final GiftRepository giftRepository;
  private final GiftClaimRepository giftClaimRepository;
  private final LoyaltyCoreClient coreClient;

  @Override
  public ResponsePage<MyGiftOutput> getInternalMyGift(
      Long customerId, String cifBank, String cifWallet, VoucherStatus type, Pageable pageable) {
    // todo chua co API dung qua

    // lấy quà
    //    if (type == GiftStatus.CLAIMED) {
    //      var page = giftClaimRepository.findByCustomerId(customerId, pageable);
    //
    //      return new ResponsePage<>(
    //          page,
    //          CollectionUtils.isEmpty(page.getContent())
    //              ? new ArrayList<>()
    //              : modelMapper.toGiftOutputs(page.getContent()));
    //    }
    //    var page = giftRepository.findAllBy(pageable);
    //    return new ResponsePage<>(
    //        page,
    //        CollectionUtils.isEmpty(page.getContent())
    //            ? new ArrayList<>()
    //            : modelMapper.convertToGiftOutputs(page.getContent()));
    return new ResponsePage<>(
        1,
        10,
        2L,
        1,
        Arrays.asList(
            MyGiftOutput.builder()
                .id(2L)
                .name("(TEST) Phòng khách hạng thương gia - Chuyến bay Quốc nội")
                .price(1000L)
                .endDate(LocalDate.now().plusDays(30))
                .thumbnail(
                    "https://lounge.mpoint.vn/images/eda5fcca-9884-4c07-88db-aba172277011.jpg")
                .totalPoint(1000L)
                .quantity(1L)
                .claimsAt(LocalDateTime.now().plusDays(3))
                .voucherStatus(VoucherStatus.CLAIMED)
                .build(),
            MyGiftOutput.builder()
                .id(4L)
                .name("(TEST) Phòng khách hạng thương gia - Chuyến bay Quốc nội")
                .price(1000L)
                .endDate(LocalDate.now().plusDays(30))
                .thumbnail(
                    "https://lounge.mpoint.vn/images/eda5fcca-9884-4c07-88db-aba172277011.jpg")
                .totalPoint(1000L)
                .quantity(1L)
                .claimsAt(LocalDateTime.now().plusDays(3))
                .voucherStatus(VoucherStatus.AVAILABLE)
                .build(),
            MyGiftOutput.builder()
                .id(5L)
                .name("(TEST KHÔNG THUỘC GROUP) Phòng khách hạng thương gia - Chuyến bay Quốc nội")
                .price(1000L)
                .endDate(LocalDate.now().plusDays(30))
                .thumbnail(
                    "https://lounge.mpoint.vn/images/eda5fcca-9884-4c07-88db-aba172277011.jpg")
                .totalPoint(1000L)
                .quantity(1L)
                .claimsAt(LocalDateTime.now().plusDays(3))
                .voucherStatus(VoucherStatus.USED)
                .build()));
  }

  @Override
  public GiftClaimOutput internalClaimsGift(ClaimGiftInput input) {
    var requestId = ThreadContext.get(RequestConstant.REQUEST_ID);

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

  @Override
  public GiftClaimOutput internalCheckStatus(String refNo) {
    return null;
  }
}
