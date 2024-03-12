package vn.com.atomi.loyalty.gift.repository.redis;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.base.utils.JsonUtils;
import vn.com.atomi.loyalty.gift.dto.output.CategoryOutput;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class CategoryCacheRepository {

  private static final String KEY_GIFT_CATEGORY_ALL = "LOYALTY_GIFT_CATEGORY_ACTIVE_ALL";
  private final RedisTemplate<String, Object> redisTemplate;

  public List<CategoryOutput> getCategories() {
    var opt = (String) this.redisTemplate.opsForValue().get(KEY_GIFT_CATEGORY_ALL);
    return opt == null
        ? new ArrayList<>()
        : JsonUtils.fromJson(opt, List.class, CategoryOutput.class);
  }
}
