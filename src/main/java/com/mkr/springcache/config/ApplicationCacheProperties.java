package com.mkr.springcache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Время кэширования устанавливается в файле application.yml
@Data
@ConfigurationProperties(prefix = "app.cache") // Добавляем настройки для app.cache
public class ApplicationCacheProperties {

    private final List<String> cacheNames = new ArrayList<>();

    private final Map<String, CacheProperties> caches = new HashMap<>();

    private CacheType cacheType; // app.cache.cacheType - у этого поля теперь добавится возможность сетать enum-значения

    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;

    }

    public enum CacheType {
        IN_MEMORY,
        REDIS
    }
}
