package vn.com.atomi.loyalty.gift.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.gift.entity.Gift;
import vn.com.atomi.loyalty.gift.entity.GiftClaim;
import vn.com.atomi.loyalty.gift.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface GiftClaimRepository extends JpaRepository<GiftClaim, Long> {}
