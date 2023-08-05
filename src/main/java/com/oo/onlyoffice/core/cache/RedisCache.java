package com.oo.onlyoffice.core.cache;

import com.oo.onlyoffice.core.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: onlyoffice-demo
 * @BelongsPackage: com.oo.onlyoffice.core.cache
 * @Author: TongHui
 * @CreateTime: 2023-08-01 18:06
 * @Description: TODO
 * @Version: 1.0
 */

public class RedisCache implements Cache {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
