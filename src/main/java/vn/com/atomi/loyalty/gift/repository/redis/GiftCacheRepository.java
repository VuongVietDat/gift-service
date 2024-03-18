package vn.com.atomi.loyalty.gift.repository.redis;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.base.utils.JsonUtils;
import vn.com.atomi.loyalty.gift.dto.output.CategoryOutput;
import vn.com.atomi.loyalty.gift.dto.output.GiftOutput;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class GiftCacheRepository {
  private final RedisTemplate<String, Object> redisTemplate;

  private String composeHeader(Long categoryId) {
    return "LOYALTY_GIFT_" + (categoryId == null ? "ALL" : String.valueOf(categoryId));
  }

  public List<GiftOutput> gets(Long categoryId) {
    var opt = (String) this.redisTemplate.opsForValue().get(composeHeader(categoryId));
    return opt == null ? new ArrayList<>() : JsonUtils.fromJson(opt, List.class, GiftOutput.class);
  }

  public void put(Long categoryId, List<GiftOutput> outputs) {
    this.redisTemplate.opsForValue().set(composeHeader(categoryId), JsonUtils.toJson(outputs));
  }
}
