package vn.com.atomi.loyalty.gift.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.gift.dto.output.GiftApplyAddressOutput;
import vn.com.atomi.loyalty.gift.entity.Gift;
import vn.com.atomi.loyalty.gift.entity.GiftApplyAddress;
import vn.com.atomi.loyalty.gift.enums.Status;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface GiftApplyAddressRepository extends JpaRepository<GiftApplyAddress, Long> {
  List<GiftApplyAddress> findAllByGiftId(Long giftId);
}
