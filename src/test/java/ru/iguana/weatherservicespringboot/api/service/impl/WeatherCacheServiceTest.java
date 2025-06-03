package ru.iguana.weatherservicespringboot.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherCacheServiceTest {

    @Mock
    private RedisTemplate<String, CityDto> redisTemplate;

    @Mock
    private ValueOperations<String, CityDto> valueOperations;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private WeatherCacheService weatherCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("get() should retrieve data from Redis for popular city")
    void get_shouldUseRedisForPopularCity() {
        CityDto dto = new CityDto();
        dto.setName("Москва");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("Москва")).thenReturn(dto);

        CityDto result = weatherCacheService.get("Москва");

        assertNotNull(result);
        assertEquals("Москва", result.getName());
        verify(redisTemplate).opsForValue();
        verify(valueOperations).get("Москва");
        verifyNoInteractions(cacheManager);
    }

    @Test
    @DisplayName("put() should store data in Redis for popular city")
    void put_shouldUseRedisForPopularCity() {
        CityDto dto = new CityDto();
        dto.setName("Казань");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        weatherCacheService.put("Казань", dto);

        verify(valueOperations).set("Казань", dto, Duration.ofHours(1));
        verifyNoInteractions(cacheManager);
    }

    @Test
    @DisplayName("evict() should delete from Redis for popular city")
    void evict_shouldUseRedisForPopularCity() {
        weatherCacheService.evict("Санкт-Петербург");

        verify(redisTemplate).delete("Санкт-Петербург");
        verifyNoInteractions(cacheManager);
    }

    @Test
    @DisplayName("get() should retrieve data from local cache for non-popular city")
    void get_shouldUseLocalCacheForNonPopularCity() {
        CityDto dto = new CityDto();
        dto.setName("Томск");

        when(cacheManager.getCache("cityWeather")).thenReturn(cache);
        when(cache.get("Томск", CityDto.class)).thenReturn(dto);

        CityDto result = weatherCacheService.get("Томск");

        assertNotNull(result);
        assertEquals("Томск", result.getName());
        verify(cacheManager).getCache("cityWeather");
        verify(cache).get("Томск", CityDto.class);
        verifyNoInteractions(redisTemplate);
    }

    @Test
    @DisplayName("put() should store data in local cache for non-popular city")
    void put_shouldUseLocalCacheForNonPopularCity() {
        CityDto dto = new CityDto();
        dto.setName("Омск");

        when(cacheManager.getCache("cityWeather")).thenReturn(cache);

        weatherCacheService.put("Омск", dto);

        verify(cache).put("Омск", dto);
        verifyNoInteractions(redisTemplate);
    }

    @Test
    @DisplayName("evict() should evict data from local cache for non-popular city")
    void evict_shouldUseLocalCacheForNonPopularCity() {
        when(cacheManager.getCache("cityWeather")).thenReturn(cache);

        weatherCacheService.evict("Челябинск");

        verify(cache).evict("Челябинск");
        verifyNoInteractions(redisTemplate);
    }

    @Test
    @DisplayName("get() should return null if local cache is null")
    void get_shouldReturnNullIfCacheMissing() {
        when(cacheManager.getCache("cityWeather")).thenReturn(null);

        CityDto result = weatherCacheService.get("Тула");

        assertNull(result);
    }
}
