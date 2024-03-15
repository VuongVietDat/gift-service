package vn.com.atomi.loyalty.gift.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.gift.dto.input.ApprovalInput;
import vn.com.atomi.loyalty.gift.dto.input.CategoryInput;
import vn.com.atomi.loyalty.gift.dto.output.CategoryApprovalOutput;
import vn.com.atomi.loyalty.gift.dto.output.CategoryOutput;
import vn.com.atomi.loyalty.gift.dto.output.ComparisonOutput;
import vn.com.atomi.loyalty.gift.dto.output.InternalCategoryOutput;
import vn.com.atomi.loyalty.gift.entity.CategoryApproval;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.ErrorCode;
import vn.com.atomi.loyalty.gift.enums.Status;
import vn.com.atomi.loyalty.gift.repository.CategoryApprovalRepository;
import vn.com.atomi.loyalty.gift.repository.CategoryRepository;
import vn.com.atomi.loyalty.gift.service.CategoryService;
import vn.com.atomi.loyalty.gift.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends BaseService implements CategoryService {

  private final CategoryApprovalRepository categoryApprovalRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public void createCategory(CategoryInput categoryInput) {
    var startDate = Utils.convertToLocalDate(categoryInput.getStartDate());
    var endDate = Utils.convertToLocalDate(categoryInput.getEndDate());

    // tạo code
    var id = categoryApprovalRepository.getSequence();
    var code = Utils.generateCode(id, CategoryApproval.class.getSimpleName());

    // lưu bản ghi chờ duyệt
    var approval =
        modelMapper.convertToCategoryApproval(
            categoryInput,
            startDate,
            endDate,
            ApprovalStatus.WAITING,
            ApprovalType.CREATE,
            id,
            code);
    categoryApprovalRepository.save(approval);
  }

  @Override
  public ResponsePage<CategoryApprovalOutput> getCategoryApprovals(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String startApprovedDate,
      String endApprovedDate,
      String name,
      String code,
      Pageable pageable) {
    var stDate = Utils.convertToLocalDateTimeStartDay(startApprovedDate);
    var edDate = Utils.convertToLocalDateTimeEndDay(endApprovedDate);
    // nếu tìm kiếm theo khoảng ngày phê duyệt thì trạng thái phê duyệt phải là đồng ý hoặc từ chối
    if ((stDate != null || edDate != null)
        && (ApprovalStatus.WAITING.equals(approvalStatus)
            || ApprovalStatus.RECALL.equals(approvalStatus))) {
      return new ResponsePage<>(
          pageable.getPageNumber(), pageable.getPageSize(), 0, 0, new ArrayList<>());
    }

    var categoryPage =
        categoryApprovalRepository.findByCondition(
            status,
            approvalStatus,
            approvalType,
            Utils.makeLikeParameter(name),
            Utils.makeLikeParameter(code),
            stDate,
            edDate,
            pageable);

    if (CollectionUtils.isEmpty(categoryPage.getContent()))
      return new ResponsePage<>(categoryPage, new ArrayList<>());

    return new ResponsePage<>(
        categoryPage,
        modelMapper.convertToCategoryOutputsApprovalOutputs(categoryPage.getContent()));
  }

  @Override
  public CategoryApprovalOutput getCategoryApproval(Long id) {
    var campaignApproval =
        categoryApprovalRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED));
    return super.modelMapper.convertToCategoryOutputOutput(campaignApproval);
  }

  @Override
  public ResponsePage<CategoryOutput> getCategories(
      Status status, String name, String code, Pageable pageable) {
    var campaignPage = categoryRepository.findByCondition(code, name, status, pageable);
    return new ResponsePage<>(
        campaignPage,
        super.modelMapper.convertToCategoryApprovalOutputs(campaignPage.getContent()));
  }

  @Override
  public CategoryOutput getCategory(Long id) {
    var campaign =
        categoryRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));
    return super.modelMapper.convertToCategoryOutput(campaign);
  }

  @Override
  public void approveCategory(ApprovalInput input) {
    // tìm kiếm bản ghi chờ duyệt
    var categoryApproval =
        categoryApprovalRepository
            .findByDeletedFalseAndIdAndApprovalStatus(input.getId(), ApprovalStatus.WAITING)
            .orElseThrow(() -> new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED));
    if (input.getIsAccepted()) {
      // trường hợp phê duyệt tạo
      if (ApprovalType.CREATE.equals(categoryApproval.getApprovalType())) {
        // lưu thông tin
        var record = super.modelMapper.convertToCategory(categoryApproval);
        record = categoryRepository.save(record);
        categoryApproval.setCategoryId(record.getId());
      }

      // trường hợp phê duyệt cập nhật
      if (ApprovalType.UPDATE.equals(categoryApproval.getApprovalType())) {
        // lấy thông tin hiện tại
        var current =
            categoryRepository
                .findByDeletedFalseAndId(categoryApproval.getCategoryId())
                .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));
        current = super.modelMapper.mapToCategory(current, categoryApproval);
        categoryRepository.save(current);
      }
    }
    // cập nhật trạng thái bản ghi chờ duyệt
    categoryApproval.setApprovalStatus(
        input.getIsAccepted() ? ApprovalStatus.ACCEPTED : ApprovalStatus.REJECTED);
    categoryApproval.setApprovalComment(input.getComment());
    categoryApprovalRepository.save(categoryApproval);
  }

  @Override
  public void updateCategory(Long id, CategoryInput categoryInput) {
    // lấy record hiện tại
    var record =
        categoryRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));

    var newCampaign = super.modelMapper.convertToCampaign(record, categoryInput);
    // tạo bản ghi chờ duyệt
    var campaignApproval =
        super.modelMapper.mapToCategoryApproval(
            newCampaign, ApprovalStatus.WAITING, ApprovalType.UPDATE);
    categoryApprovalRepository.save(campaignApproval);
  }

  @Override
  public void recallCategoryApproval(Long id) {
    // chỉ được thu hồi những bản ghi ở trạng thái chờ duyệt
    categoryApprovalRepository
        .findByDeletedFalseAndIdAndApprovalStatus(id, ApprovalStatus.WAITING)
        .ifPresentOrElse(
            ruleApproval -> {
              ruleApproval.setApprovalStatus(ApprovalStatus.RECALL);
              categoryApprovalRepository.save(ruleApproval);
            },
            () -> {
              throw new BaseException(ErrorCode.APPROVING_RECORD_NOT_EXISTED);
            });
  }

  @Override
  public List<ComparisonOutput> geCategoryApprovalComparison(Long id) {
    // tìm kiếm bản ghi duyệt cập nhật
    var newApproval =
        categoryApprovalRepository
            .findByDeletedFalseAndId(id)
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));
    if (!ApprovalType.UPDATE.equals(newApproval.getApprovalType())) {
      throw new BaseException(ErrorCode.APPROVE_TYPE_NOT_MATCH_UPDATE);
    }
    // tìm kiếm bản ghi đã phê duyệt gần nhất
    var oldApproval =
            categoryApprovalRepository
            .findLatestAcceptedRecord(newApproval.getCategoryId(), id)
            .orElseThrow(() -> new BaseException(ErrorCode.RECORD_NOT_EXISTED));
    // thực hiện so sánh
    DiffResult<CategoryApproval> diffResult =
        new DiffBuilder<>(oldApproval, newApproval, ToStringStyle.DEFAULT_STYLE)
            .append(
                "startDate",
                Utils.formatLocalDateToString(oldApproval.getStartDate()),
                Utils.formatLocalDateToString(newApproval.getStartDate()))
            .append(
                "endDate",
                Utils.formatLocalDateToString(oldApproval.getEndDate()),
                Utils.formatLocalDateToString(newApproval.getEndDate()))
            .append("status", oldApproval.getStatus(), newApproval.getStatus())
            .append("name", oldApproval.getName(), newApproval.getName())
            .build();
    return diffResult.getDiffs().stream()
        .map(
            diff ->
                ComparisonOutput.builder()
                    .fileName(diff.getFieldName())
                    .oldValue(diff.getLeft() == null ? null : diff.getLeft().toString())
                    .newValue(diff.getRight() == null ? null : diff.getRight().toString())
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  public List<InternalCategoryOutput> getInternalCategories() {
    return null;
  }
}
