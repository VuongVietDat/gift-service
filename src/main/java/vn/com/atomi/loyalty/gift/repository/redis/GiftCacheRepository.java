package vn.com.atomi.loyalty.gift.repository.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.base.data.ResponsePage;
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
  private static final String KEY_PREFIX = "LOYALTY_GIFT_";
  private final RedisTemplate<String, Object> redisTemplate;

  private String composeHeader(Long categoryId) {
    return KEY_PREFIX + (categoryId == null ? "ALL" : String.valueOf(categoryId));
  }

  public Optional<ResponsePage<GiftOutput>> gets(Long categoryId) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(composeHeader(categoryId)))
        .map(o -> JsonUtils.fromJson((String) o, ResponsePage.class, GiftOutput.class));
  }

  public void put(Long categoryId, ResponsePage<GiftOutput> outputs) {
    redisTemplate
        .opsForValue()
        .set(composeHeader(categoryId), JsonUtils.toJson(outputs), 15, TimeUnit.MINUTES);
  }

  public void clear() {
    var keys = redisTemplate.keys(KEY_PREFIX + '*');
    if (keys != null) redisTemplate.delete(keys);
  }
}
