package vn.com.atomi.loyalty.gift.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.gift.entity.Category;
import vn.com.atomi.loyalty.gift.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByDeletedFalseAndIdAndStatus(Long id, Status status);

  List<Category> findByDeletedFalseAndStatusOrderByOrderNo(Status status);

  Optional<Category> findByDeletedFalseAndId(Long id);

  //  @Query(
  //      value =
  //          "select c "
  //              + "from Category c "
  //              + "where c.deleted = false "
  //              + "  and (:status is null or c.status = :status) "
  //              + "  and (:startDate is null or c.startDate >= :startDate) "
  //              + "  and (:endDate is null or c.endDate <= :endDate) "
  //              + "order by c.updatedAt desc ")
  //  Page<Category> findByCondition(
  //      Status status, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
