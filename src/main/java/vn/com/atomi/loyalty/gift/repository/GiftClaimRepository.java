package vn.com.atomi.loyalty.gift.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.gift.dto.projection.GiftProjection;
import vn.com.atomi.loyalty.gift.entity.GiftClaim;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface GiftClaimRepository extends JpaRepository<GiftClaim, Long> {

//  @Query("select g from Gift g join GiftClaim gc on g.id = gc.giftId" +
//          " where gc.customerId = :customerId order by g.updatedAt desc ")
//  Page<GiftProjection> findByCustomerId(Long customerId, Pageable pageable);

  @Query("select gc from GiftClaim gc " +
          "where gc.customerId = :customerId " +
          "and ((:type is null) " +
          "or (:type = 'AVAILABLE' and gc.voucherStatus = 'AVAILABLE') " +
          "or (:type = 'USED' and (gc.voucherStatus = 'USED' or gc.voucherStatus = 'EXPIRED'))) " +
          "order by gc.updatedAt desc")
  Page<GiftClaim> findByCustomerIdAndType(Long customerId, String type, Pageable pageable);

  @Query("select gc from GiftClaim gc " +
          "where gc.customerId = :customerId " +
          "order by gc.updatedAt desc")
  Page<GiftClaim> findByCustomerId(Long customerId, Pageable pageable);

}
