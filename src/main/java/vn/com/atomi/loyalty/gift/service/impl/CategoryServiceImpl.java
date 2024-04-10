package vn.com.atomi.loyalty.gift.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.gift.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.gift.dto.input.CategoryInput;
import vn.com.atomi.loyalty.gift.dto.output.CategoryOutput;
import vn.com.atomi.loyalty.gift.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.gift.dto.output.InternalCategoryOutput;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.Status;
import vn.com.atomi.loyalty.gift.repository.CategoryRepository;
import vn.com.atomi.loyalty.gift.service.CategoryService;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends BaseService implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public void createCategory(CategoryInput categoryInput) {}

  @Override
  public ResponsePage<CategoryOutput> getCategoryApprovals(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String startApprovedDate,
      String endApprovedDate,
      String name,
      String code,
      Pageable pageable) {
    return null;
  }

  @Override
  public CategoryOutput getCategoryApproval(Long id) {
    return null;
  }

  @Override
  public ResponsePage<CategoryOutput> getCategories(
      Status status, String name, String code, Pageable pageable) {
    //    var categoryPage =
    //        categoryRepository.findByCondition(
    //            status,
    //            pageable);
    //    return new ResponsePage<>(
    //        categoryPage, super.modelMapper.convertToCategoryOutput(categoryPage.getContent()));
    return null;
  }

  @Override
  public CategoryOutput getCategory(Long id) {
    return null;
  }

  @Override
  public void approveCategory(ApprovalInput input) {}

  @Override
  public void updateCategory(Long id, CategoryInput categoryInput) {}

  @Override
  public void recallCategoryApproval(Long id) {}

  @Override
  public List<ComparisonOutput> geCategoryApprovalComparison(Long id) {
    return null;
  }

  @Override
  public List<InternalCategoryOutput> getInternalCategories() {
    return super.modelMapper.convertToInternalCategoryOutputs(
        categoryRepository.findByDeletedFalseAndStatus(Status.ACTIVE));
  }
}
