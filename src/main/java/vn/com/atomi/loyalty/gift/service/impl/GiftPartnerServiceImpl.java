package vn.com.atomi.loyalty.gift.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.gift.dto.input.GiftPartnerInput;
import vn.com.atomi.loyalty.gift.dto.output.*;
import vn.com.atomi.loyalty.gift.entity.GiftPartner;
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

//    Boolean isCheckCode = giftPartnerRepository.existsByCode(input.getCode());
//    if (isCheckCode) {
      // Lấy code đối tác từ input
      String partnerCode = input.getPartnerCode(); // partnerCode phải được lấy từ input

      // Lấy mã cuối cùng từ GiftPartner
      var lastGiftPartner = giftPartnerRepository.findTopByOrderByCreatedAtDesc();

      // Lấy mã code cuối cùng hoặc mặc định
      String lastCode = lastGiftPartner
              .map(GiftPartner::getCode)
              .orElse("GIFT_" + partnerCode + "_000000");

      // Tách phần số từ mã cuối cùng
      String numberPart = lastCode.replaceAll("\\D", "");
      int newNumber = Integer.parseInt(numberPart) + 1;

      // Tạo mã mới với định dạng
      String newCode = "GIFT_" + partnerCode + "_" + String.format("%06d", newNumber);
      input.setCode(newCode); // Set mã mới cho input
//    } else {
//      // Nếu mã chưa tồn tại, không cần tạo mã mới (giữ nguyên input.getCode())
//      input.setCode(input.getCode());
//    }

    // check category
    categoryRepository
            .findByDeletedFalseAndIdAndStatus(input.getCategoryId(), Status.ACTIVE)
            .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_EXISTED));
    // lưu bản ghi
    var giftPartner = modelMapper.convertToGiftPartner(input, startDate, endDate, effectiveDate, expiredDate);
    giftPartner.setCode(input.getCode());
    giftPartner.setQtyAssign((long) 0);
    giftPartner.setQtyRemain((long) 0);
    giftPartnerRepository.save(giftPartner);
  }
  
  public ResponsePage<GiftPartnerOutput> getGiftPartners(Status status,String effectiveDate, String name, Long partnerId,String categoryCode,Long categoryId, Pageable pageable) {
    var page = giftPartnerRepository.findListGiftPartner(status, Utils.convertToLocalDate(effectiveDate), name , partnerId ,categoryCode, categoryId, pageable);
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


//  public String getNewGiftPartnerCode() {
//    var latestGiftPartner = giftPartnerRepository.findTopByOrderByCreatedAtDesc();
//    Long partnerId = latestGiftPartner
//            .map(GiftPartner::getPartnerId) // Lấy partnerId
//            .orElse(null);
//    String partnerCode = partnerId != null
//            ? partnerRepository.findById(partnerId)
//            .map(Partner::getCode) // Lấy mã code
//            .orElse("DEFAULT")     // Nếu không tìm thấy Partner
//            : "DEFAULT";
//    // Lấy mã đối tác (partner code) từ GiftPartner hoặc giá trị mặc định
//    String partnerCode = latestGiftPartner
//            .map(GiftPartner::getPartner)          // Lấy đối tượng Partner
//            .map(Partner::getCode)                // Lấy mã code từ Partner
//            .orElse("DEFAULT");                   // Nếu không có, dùng "DEFAULT"
//
//    // Lấy mã code cuối cùng hoặc giá trị mặc định
//    String lastCode = latestGiftPartner
//            .map(GiftPartner::getCode)            // Lấy mã code từ GiftPartner
//            .orElse("GIFT_" + partnerCode + "_000000"); // Giá trị mặc định
//
//    // Tách phần tiền tố và phần số từ mã code
//    String prefix = lastCode.replaceAll("\\d", ""); // Lấy phần tiền tố
//    String numberPart = lastCode.replaceAll("\\D", ""); // Lấy phần số
//
//    // Xử lý phần số, tăng giá trị lên 1
//    int newNumber = 1; // Giá trị mặc định nếu không có phần số
//    try {
//      newNumber = Integer.parseInt(numberPart) + 1;
//    } catch (NumberFormatException e) {
//      System.err.println("Invalid number part: " + numberPart);
//    }
//
//    // Tạo mã mới
//    return "GIFT_" + partnerCode + "_" + String.format("%06d", newNumber);
  }