package com.bulbas23r.client.hub.global.cache;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration
            .defaultCacheConfig()
            // 캐시 값에 Null 허용.
            .disableCachingNullValues()
            // TTL(Time to Live)를 120초 설정.
            .entryTtl(Duration.ofSeconds(120))
            // 캐시에 붙을 prefix를 설정. `CacheKeyPrefix.simple()`의 경우 `::`로 설정.
            .computePrefixWith(CacheKeyPrefix.simple())
            // 캐시에 저장할 값을 어떻게 직렬화 할 것인지. RedisSerializer.java() 사용.
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
            );

        return RedisCacheManager
            .builder(connectionFactory)
            // 모든 캐시에 기본적으로 저장.
            .cacheDefaults(config)
            .build();
    }
}
