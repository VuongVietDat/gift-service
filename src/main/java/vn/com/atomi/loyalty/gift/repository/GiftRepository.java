package vn.com.atomi.loyalty.gift.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.gift.dto.projection.GiftProjection;
import vn.com.atomi.loyalty.gift.entity.Gift;
import vn.com.atomi.loyalty.gift.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
  @Query(value = "select {h-schema}" + Gift.GENERATOR + ".nextval from DUAL", nativeQuery = true)
  Long getSequence();

  Optional<Gift> findByDeletedFalseAndId(Long id);

  Page<GiftProjection> findAllBy(Pageable pageable);

  Page<Gift> findAllByCategoryId(Long categoryId, Pageable pageable);

  boolean existsByIdAndPrice(Long id, Long price);

  @Query(
      value =
          "select c "
              + "from Gift c  "
              + "where c.deleted = false "
              + "  and (:code is null or c.code = :code) "
              + "  and (:name is null or c.code = :name) "
              + "  and (:status is null or c.status = :status) "
              + "order by c.updatedAt desc ")
  Page<Gift> findByCondition(String name, String code, Status status, Pageable pageable);
}
