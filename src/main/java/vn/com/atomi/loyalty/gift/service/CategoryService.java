package vn.com.atomi.loyalty.gift.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.gift.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.gift.dto.input.CategoryInput;
import vn.com.atomi.loyalty.gift.dto.output.CategoryApprovalOutput;
import vn.com.atomi.loyalty.gift.dto.output.CategoryOutput;
import vn.com.atomi.loyalty.gift.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.gift.dto.output.InternalCategoryOutput;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
public interface CategoryService {

  void createCategory(CategoryInput categoryInput);

  ResponsePage<CategoryApprovalOutput> getCategoryApprovals(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String startApprovedDate,
      String endApprovedDate,
      String name,
      String code,
      Pageable pageable);

  CategoryApprovalOutput getCategoryApproval(Long id);

  ResponsePage<CategoryOutput> getCategories(
      Status status, String name, String code, Pageable pageable);

  CategoryOutput getCategory(Long id);

  void approveCategory(ApprovalInput input);

  void updateCategory(Long id, CategoryInput categoryInput);

  void recallCategoryApproval(Long id);

  List<ComparisonOutput> geCategoryApprovalComparison(Long id);

  List<InternalCategoryOutput> getInternalCategories();
}
