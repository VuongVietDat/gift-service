package vn.com.atomi.loyalty.gift.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.gift.entity.Category;
import vn.com.atomi.loyalty.gift.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query(value = "select c from Category c where c.id = :id and c.status = :status",nativeQuery = true)
  Optional<Category> findByDeletedFalseAndIdAndStatus(Long id, Status status);

  @Query(value = "select c from Category c where c.status = :status",nativeQuery = true)
  List<Category> findByDeletedFalseAndStatusOrderByOrderNo(Status status);

  @Query(value = "select c from Category c where c.id = :id and ",nativeQuery = true)
  Optional<Category> findByDeletedFalseAndId(Long id);

    @Query(
        value =
            "select c "
                + "from Category c "
                + "where ( :status is null or c.status = :status) "
                + "order by c.updatedAt desc ")
    Page<Category> findByCondition(
        Status status, Pageable pageable);
}
