package vn.com.atomi.loyalty.gift.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.gift.dto.projection.CategoryApprovalProjection;
import vn.com.atomi.loyalty.gift.entity.Category;
import vn.com.atomi.loyalty.gift.entity.CategoryApproval;
import vn.com.atomi.loyalty.gift.enums.ApprovalStatus;
import vn.com.atomi.loyalty.gift.enums.ApprovalType;
import vn.com.atomi.loyalty.gift.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CategoryApprovalRepository extends JpaRepository<CategoryApproval, Long> {
  @Query(value = "select {h-schema}gs_category_arv_id_seq.nextval from DUAL", nativeQuery = true)
  Long getSequence();

  Optional<CategoryApproval> findByDeletedFalseAndId(Long id);

  Optional<CategoryApproval> findByDeletedFalseAndIdAndApprovalStatus(
      Long id, ApprovalStatus status);

  @Query(
      "select r from CategoryApproval r where r.deleted = false "
              + "and r.approvalStatus = vn.com.atomi.loyalty.gift.enums.ApprovalStatus.ACCEPTED "
              + "and r.categoryId = ?1 and r.id < ?2 "
              + "order by r.updatedAt desc "
              + "limit 1")
  Optional<CategoryApproval> findLatestAcceptedRecord(Long campaignId, Long id);

  @Query(
      value =
          "select cam.id           as id, "
              + "       cam.code         as code, "
              + "       cam.name         as name, "
              + "       cam.description         as description, "
              + "       cam.status       as status, "
              + "       cam.startDate    as startDate, "
              + "       cam.endDate      as endDate, "
              + "       cam.approvalStatus as approvalStatus, "
              + "       cam.approvalComment as approvalComment, "
              + "       cam.approvalType as approvalType, "
              + "       cam.approver as approver, "
              + "       cam.createdAt      as creationDate, "
              + "       cam.createdBy    as creator "
              + "from CategoryApproval cam "
              + "where cam.deleted = false "
              + "  and (:status is null or cam.status = :status) "
              + "  and (:approvalStatus is null or cam.approvalStatus = :approvalStatus)"
              + "  and (:approvalType is null or cam.approvalType = :approvalType)"
              + "  and (:startDate is null or cam.startDate >= :startDate) "
              + "  and (:endDate is null or cam.endDate <= :endDate) "
              + "  and (:name is null or cam.name like :name) "
              + "  and (:code is null or cam.code like :code) "
              + "  and (:startApprovedDate is null or (cam.updatedAt >= :startApprovedDate "
              + "       and cam.approvalStatus in (vn.com.atomi.loyalty.gift.enums.ApprovalStatus.ACCEPTED, vn.com.atomi.loyalty.gift.enums.ApprovalStatus.REJECTED))) "
              + "  and (:endApprovedDate is null or (cam.updatedAt >= :endApprovedDate "
              + "       and cam.approvalStatus in (vn.com.atomi.loyalty.gift.enums.ApprovalStatus.ACCEPTED, vn.com.atomi.loyalty.gift.enums.ApprovalStatus.REJECTED))) "
              + "order by cam.updatedAt desc ")
  Page<CategoryApprovalProjection> findByCondition(
      Status status,
      ApprovalStatus approvalStatus,
      ApprovalType approvalType,
      String name,
      String code,
      LocalDateTime startApprovedDate,
      LocalDateTime endApprovedDate,
      Pageable pageable);
}
