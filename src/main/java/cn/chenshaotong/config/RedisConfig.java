package cn.chenshaotong.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis相关配置
 *
 * @author lzhpo
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  /** 默认缓存有效期，单位：秒，默认七天 */
  @Value("${spring.redis.default-expired:604800}")
  private long defaultExpired;

  /** 缓存前缀 */
  @Value("${spring.redis.cache-default-prefix:magpie-caches:}")
  private String cacheDefPrefix;

  /** 缓存数据时Key的生成器 */
  @Bean
  @Override
  public KeyGenerator keyGenerator() {
    return (target, method, params) -> {
      StringBuffer sb = new StringBuffer();
      // 类名+方法名
      sb.append(target.getClass().getName());
      sb.append(".").append(method.getName());
      for (Object obj : params) {
        sb.append(obj);
      }
      return sb.toString();
    };
  }

  @Bean
  @Primary
  public CacheManager cacheManager(RedisConnectionFactory factory) {
    // 配置序列化（解决乱码的问题）
    RedisCacheConfiguration config =
        RedisCacheConfiguration.defaultCacheConfig()
            // 缓存前缀
            .computePrefixWith(cacheName -> cacheDefPrefix + cacheName)
            // 缓存有效期，单位：秒
            .entryTtl(Duration.ofSeconds(defaultExpired))
            // 使用StringRedisSerializer来序列化和反序列化redis的key值
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer()))
            // 禁用空值
            .disableCachingNullValues();

    return new CustomRedisCacheManager(
        RedisCacheWriter.nonLockingRedisCacheWriter(factory), config);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    // 用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
    redisTemplate.setValueSerializer(serializer());

    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    // 使用StringRedisSerializer来序列化和反序列化redis的key值
    redisTemplate.setKeySerializer(stringRedisSerializer);
    // hash的key也采用String的序列化方式
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    // hash的value序列化方式采用jackson
    redisTemplate.setHashValueSerializer(serializer());
    redisTemplate.afterPropertiesSet();

    return redisTemplate;
  }

  /** 配置Jackson2JsonRedisSerializer序列化策略 */
  private Jackson2JsonRedisSerializer<Object> serializer() {
    ObjectMapper objectMapper = new ObjectMapper();

    // 属性为 空（“”） 或者为 NULL 都不序列化
    objectMapper.setSerializationInclusion(Include.NON_EMPTY);

    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

    // 忽略不存在的字段
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    //    Hibernate5Module hibernate5Module = new Hibernate5Module();
    //    // 禁用(表示要忽略@Transient字段属性,默认为true,设置为false禁用)
    //    hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
    //    // 延时加载的对象不使用时设置为null
    //    hibernate5Module.enable(
    //        Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
    //    objectMapper.registerModule(hibernate5Module);

    // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(Object.class);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

    return jackson2JsonRedisSerializer;
  }
}
