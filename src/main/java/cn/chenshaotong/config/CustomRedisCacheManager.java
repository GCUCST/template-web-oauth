package cn.chenshaotong.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.StringUtils;

/**
 * 拓展RedisCacheManager，缓存注解支持失效时间TTL
 *
 * <p>Eg：@Cacheable(cacheNames = "abc#3600", key = "123", unless="#result == null")
 *
 * <p>key: abc123 ttl: 3600, 失效时间为3600秒
 *
 * @author lzhpo
 */
@Slf4j
public class CustomRedisCacheManager extends RedisCacheManager {

  public CustomRedisCacheManager(
      RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
    super(cacheWriter, defaultCacheConfiguration);
  }

  @Override
  protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
    log.info(">>> createRedisCache name: {}", name);
    String[] array = StringUtils.delimitedListToStringArray(name, "#");
    name = array[0];
    if (array.length > 1) {
      long ttl = Long.parseLong(array[1]);
      log.info(">>> ttl: {}", ttl);
      cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
    }
    return super.createRedisCache(name, cacheConfig);
  }
}
