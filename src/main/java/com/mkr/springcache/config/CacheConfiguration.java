package com.mkr.springcache.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableConfigurationProperties(ApplicationCacheProperties.class)
public class CacheConfiguration {

    @Bean
    @ConditionalOnExpression("'${app.cache.cacheType}'.equals('inMemory')")
    public ConcurrentMapCacheManager inMemoryCacheManager(ApplicationCacheProperties applicationCacheProperties) {
        var cacheManager = new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(applicationCacheProperties.getCaches().get(name).getExpiry())
                                .build().asMap(),
                        true
                );
            }
        };

        var cacheNames = applicationCacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            cacheManager.setCacheNames(cacheNames);
        }

        return cacheManager;
    }

    @Bean
    @ConditionalOnExpression("'${app.cache.cacheType}'.equals('redis')")
    public CacheManager redisCacheManager(
            ApplicationCacheProperties applicationCacheProperties,
            JedisConnectionFactory  jedisConnectionFactory) {
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        applicationCacheProperties.getCacheNames().forEach(cacheName -> cacheConfigs.put(
                cacheName,
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                        applicationCacheProperties.getCaches().get(cacheName).getExpiry()
                )
        ));

        return RedisCacheManager.builder(jedisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}
