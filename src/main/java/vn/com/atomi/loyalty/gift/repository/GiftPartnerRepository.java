package vn.com.atomi.loyalty.gift.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.gift.dto.output.GiftPartnerOutput;
import vn.com.atomi.loyalty.gift.dto.projection.GiftProjection;
import vn.com.atomi.loyalty.gift.entity.Gift;
import vn.com.atomi.loyalty.gift.entity.GiftPartner;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author nghiatd
 * @version 1.0
 */
@Repository
public interface GiftPartnerRepository extends JpaRepository<GiftPartner, Long> {

  @Query(value = "select {h-schema}" + Gift.GENERATOR + ".nextval from DUAL", nativeQuery = true)
  Long getSequence();
  @Query(value =
          "select cp from GiftPartner cp where " +
                  " cp.id= :id " +
                  " and cp.deleted=:false" +
                  " order by cp.updatedAt desc",
          nativeQuery = true)
  Optional<GiftPartner> findByDeletedFalseAndId(Long id);

  @Query(
      value =
          "select cp "
              + "from GiftPartner cp left join Category ca on ca.id = cp.categoryId "
              + "where cp.deleted = false "
                  + " and (:categoryId is null or cp.categoryId = :categoryId) "
                  + " and (:name is null or lower(cp.name) like lower('%' || :name || '%')) "
                  + " and (:status is null or cp.status = :status)"
                  + " and (:categorycode is null or lower(ca.code) like lower('%' || :categorycode || '%')) ")
  Page<GiftPartner> findByCondition(Long categoryId, String name, Status status,String categorycode, Pageable pageable);

  @Query(
          value = "select cp " +
                  "from GiftPartner cp " +
                  "left join Category ca on ca.id = cp.categoryId " +
                  "where cp.deleted = false " +
                  "and (:categoryId is null or cp.categoryId = :categoryId) " +
                  "and (:name is null or lower(cp.name) like concat('%', lower(:name), '%')) " +
                  "and (:status is null or cp.status = :status) " +
                  "and (:categoryCode is null or lower(ca.code) like concat('%', lower(:categoryCode), '%'))"
  )
  List<GiftPartner> findListGiftPartnerByCondition(
          Long categoryId,
          String name,
          Status status,
          String categoryCode
  );

  @Query(
          value =
                  "select cp "
                          + "from GiftPartner cp "
                          + "where cp.deleted = false "
                          + "  and (:categoryId is null or cp.categoryId = :categoryId) "
                          + "  and (:status is null or cp.status = :status)")
  Page<GiftPartner> findAllByCategoryId(Long categoryId, Pageable pageable);

  @Query(
          value =
                  "select cp "
                          + "from GiftPartner cp left join Category ca on ca.id = cp.categoryId "
                          + "where cp.deleted = false "
                          + " and (:categoryId is null or cp.categoryId = :categoryId) "
                          + " and (:name is null or lower(cp.name) like lower('%' || :name || '%')) "
                          + " and (:status is null or cp.status = :status) "
                          + " and (:partnerId is null or cp.partnerId = :partnerId) "
                          + " and (:effectiveDate is null or cp.effectiveDate >= :effectiveDate)"
                          + " and (:categoryCode is null or lower(ca.code) like lower('%' || :categoryCode || '%')) ")
  Page<GiftPartner> findListGiftPartner(Status status, LocalDate effectiveDate, String name, Long partnerId ,String categoryCode, Long categoryId, Pageable pageable);


  @Query(
          value =
                  "select cp "
                          + "from GiftPartner cp left join Category ca on ca.id = cp.categoryId "
                          + "where cp.deleted = false "
                          + " and (:categoryId is null or cp.categoryId = :categoryId) "
                          + " and (:name is null or lower(cp.name) like concat('%', lower(:name), '%')) "
                          + " and (:status is null or cp.status = :status) "
                          + " and (:partnerId is null or cp.partnerId = :partnerId) "
                          + " and (:effectiveDate is null or cp.effectiveDate >= :effectiveDate)"
                          + " and (:categoryCode is null or lower(ca.code) like concat('%', lower(:categoryCode), '%'))"
  )
  List<GiftPartnerOutput> getListGiftPartner(Status status, LocalDate effectiveDate, String name, Long partnerId, String categoryCode, Long categoryId);
  @Query(
          value =
                  "select cp "
                          + "from GiftPartner cp "
                          + "where cp.deleted = false "
                          + " and (:name is null or lower(cp.name) like concat('%', lower(:name), '%')) "
                          + " and (:status is null or cp.status = :status) "
                          + " and (:effectiveDate is null or cp.effectiveDate >= :effectiveDate)"
  )
  Page<GiftPartner> getAllActiveGiftPartner(Status status, LocalDate effectiveDate, Pageable pageable);
}
