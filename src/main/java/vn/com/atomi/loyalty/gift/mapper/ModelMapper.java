package vn.com.atomi.loyalty.gift.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.*;
import vn.com.atomi.loyalty.gift.dto.input.CategoryInput;
import vn.com.atomi.loyalty.gift.dto.output.*;
import vn.com.atomi.loyalty.gift.dto.projection.CategoryApprovalProjection;
import vn.com.atomi.loyalty.gift.entity.Category;
import vn.com.atomi.loyalty.gift.entity.CategoryApproval;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;

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

  @Mapping(target = "id", source = "id")
  @Mapping(target = "code", source = "code")
  @Mapping(target = "startDate", source = "startDate")
  @Mapping(target = "endDate", source = "endDate")
  @Mapping(target = "approvalType", source = "approvalType")
  @Mapping(target = "approvalStatus", source = "approvalStatus")
  CategoryApproval convertToCategoryApproval(
      CategoryInput campaignInput,
      LocalDate startDate,
      LocalDate endDate,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      Long id,
      String code);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  Category mapToCategory(@MappingTarget Category currentCampaign, CategoryApproval content);

  Category convertToCampaign(@MappingTarget Category campaign, CategoryInput campaignInput);
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "approvalType", source = "approvalType")
  @Mapping(target = "approvalStatus", source = "approvalStatus")
  CategoryApproval mapToCategoryApproval(
          Category category, ApprovalStatus approvalStatus, ApprovalType approvalType);

  Category convertToCategory(CategoryApproval content);

  CategoryOutput convertToCategoryOutput(Category content);
  CategoryApprovalOutput convertToCategoryOutputOutput(CategoryApproval content);

  List<CategoryApprovalOutput> convertToCategoryOutputsApprovalOutputs(
      List<CategoryApprovalProjection> content);

  List<CategoryOutput> convertToCategoryApprovalOutputs(List<Category> content);
  List<InternalCategoryOutput> convertToInternalCategoryOutputs(List<Category> content);
}
