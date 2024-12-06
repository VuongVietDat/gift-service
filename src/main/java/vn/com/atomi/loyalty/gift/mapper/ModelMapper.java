package vn.com.atomi.loyalty.gift.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.*;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.input.GiftClaimInput;
import vn.com.atomi.loyalty.gift.dto.input.GiftInput;
import vn.com.atomi.loyalty.gift.dto.input.GiftPartnerInput;
import vn.com.atomi.loyalty.gift.dto.output.*;
import vn.com.atomi.loyalty.gift.dto.projection.GiftProjection;
import vn.com.atomi.loyalty.gift.entity.*;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;

/**
 * @author haidv
 * @version 1.0
 */
@Mapper
public interface ModelMapper {

  default String getApprover(ApprovalStatus approvalStatus, String updateBy) {
    return ApprovalStatus.RECALL.equals(approvalStatus)
            || ApprovalStatus.WAITING.equals(approvalStatus)
        ? null
        : updateBy;
  }

  default LocalDateTime getApproveDate(ApprovalStatus approvalStatus, LocalDateTime updateAt) {
    return ApprovalStatus.RECALL.equals(approvalStatus)
            || ApprovalStatus.WAITING.equals(approvalStatus)
        ? null
        : updateAt;
  }

  @Named("findDictionaryName")
  default String findDictionaryName(
      String code, @Context List<DictionaryOutput> dictionaryOutputs) {
    if (code == null || dictionaryOutputs == null) {
      return null;
    }
    for (DictionaryOutput value : dictionaryOutputs) {
      if (value != null && code.equals(value.getCode())) {
        return value.getName();
      }
    }
    return null;
  }

  List<CategoryOutput> convertToCategoryOutput(List<Category> content);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "code", source = "code")
  @Mapping(target = "startDate", source = "startDate")
  @Mapping(target = "endDate", source = "endDate")
  Gift convertToGift(GiftInput input, LocalDate startDate, LocalDate endDate, Long id, String code);


  GiftClaim convertToGiftGiftClaim(GiftClaimInput input);

  GiftOutput convertToGiftOutput(Gift gift);
  InternalGiftOutput convertToInternalGiftOutput(Gift gift);
  InternalGiftOutput convertPartnerGiftToGiftOutput(GiftPartner gift);
  List<GiftOutput> convertToGiftOutputs(List<Gift> gifts);
  List<InternalGiftOutput> convertToInternalGiftOutputs(List<Gift> gifts);
  List<GiftOutput> toGiftOutputs(List<GiftProjection> gifts);
  List<InternalGiftOutput> toInternalGiftOutputs(List<GiftProjection> gifts);
  Gift mappingToGift(@MappingTarget Gift gift, GiftInput giftInput);

  @Mapping(target = "giftId", source = "giftId")
  @Mapping(target = "customerId", source = "customerId")
  GiftClaim convertToGiftClaim(ClaimGiftInput input, Long giftId, Long customerId);

  GiftClaimOutput convertToGiftClaimOutput(GiftClaim claim);

  List<InternalCategoryOutput> convertToInternalCategoryOutputs(
      List<Category> byDeletedFalseAndStatus);

  GiftApplyAddressOutput convertToOutput(GiftApplyAddress applyAddress);

  @Mapping(target = "startDate", source = "startDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "endDate", source = "endDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "effectiveDate", source = "effectiveDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "expiredDate", source = "expiredDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  GiftPartner convertToGiftPartner(GiftPartnerInput input, LocalDate startDate, LocalDate endDate, LocalDate effectiveDate, LocalDate expiredDate);

  List<GiftPartnerOutput> convertToGiftPartnerOutputs(List<GiftPartner> giftPartners);

  @Mapping(target = "giftCount", source = "qtyAvail")
  @Mapping(target = "totalRemaining", source = "qtyRemain")
  @Mapping(target = "description", source = "notes")
  @Mapping(target = "termsOfUse", source = "condition")
  @Mapping(target = "images", source = "image")
  List<InternalGiftOutput> convertGiftPartnerToGiftOutputs(List<GiftPartner> giftPartners);

  @Mapping(target = "startDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "endDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "effectiveDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "expiredDate",  dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  GiftPartner mappingToGiftPartner(@MappingTarget GiftPartner giftPartner, GiftPartnerInput giftPartnerInput);

  @Mapping(target = "startDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "endDate", dateFormat = DateConstant.STR_PLAN_DD_MM_YYYY_STROKE)
  @Mapping(target = "categoryCode", ignore = true)
  GiftPartnerOutput convertToGiftPartnerOutput(GiftPartner giftPartner);
}
