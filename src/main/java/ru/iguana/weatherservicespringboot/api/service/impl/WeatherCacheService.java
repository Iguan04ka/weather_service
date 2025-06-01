package ru.iguana.weatherservicespringboot.api.service.impl;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;

import java.time.Duration;
import java.util.Set;

@Service
public class WeatherCacheService {

    private static final String CACHE_NAME = "cityWeather";
    private static final Set<String> POPULAR_CITIES = Set.of("Москва", "Санкт-Петербург", "Казань", "Новосибирск", "Екатеринбург");

    private final RedisTemplate<String, CityDto> redisTemplate;
    private final CacheManager cacheManager;

    public WeatherCacheService(RedisTemplate<String, CityDto> redisTemplate, CacheManager cacheManager) {
        this.redisTemplate = redisTemplate;
        this.cacheManager = cacheManager;
    }

    public CityDto get(String cityName) {
        if (isPopular(cityName)) {
            return redisTemplate.opsForValue().get(cityName);
        } else {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            return cache != null ? cache.get(cityName, CityDto.class) : null;
        }
    }

    public void put(String cityName, CityDto data) {
        if (isPopular(cityName)) {
            redisTemplate.opsForValue().set(cityName, data, Duration.ofHours(1));
        } else {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                cache.put(cityName, data);
            }
        }
    }

    public void evict(String cityName) {
        if (isPopular(cityName)) {
            redisTemplate.delete(cityName);
        } else {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                cache.evict(cityName);
            }
        }
    }

    private boolean isPopular(String cityName) {
        return POPULAR_CITIES.contains(cityName);
    }
}

