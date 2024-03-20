package vn.com.atomi.loyalty.gift.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.*;
import vn.com.atomi.loyalty.gift.dto.input.ClaimGiftInput;
import vn.com.atomi.loyalty.gift.dto.input.GiftInput;
import vn.com.atomi.loyalty.gift.dto.output.*;
import vn.com.atomi.loyalty.gift.entity.Category;
import vn.com.atomi.loyalty.gift.entity.Gift;
import vn.com.atomi.loyalty.gift.entity.GiftClaim;
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

  GiftOutput convertToGiftOutput(Gift gift);

  List<GiftOutput> convertToGiftOutputs(List<Gift> gifts);

  Gift mappingToGift(@MappingTarget Gift gift, GiftInput giftInput);

  @Mapping(target = "giftId", source = "giftId")
  @Mapping(target = "customerId", source = "customerId")
  GiftClaim convertToGiftClaim(ClaimGiftInput input, Long giftId, Long customerId);

  GiftClaimOutput convertToGiftClaimOutput(GiftClaim claim);
}
