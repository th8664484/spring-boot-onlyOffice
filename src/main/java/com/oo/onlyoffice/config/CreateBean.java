package com.oo.onlyoffice.config;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oo.onlyoffice.core.Cache;
import com.oo.onlyoffice.core.cache.InsideCache;
import com.oo.onlyoffice.core.cache.RedisCache;
import com.oo.onlyoffice.tools.LoadConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @BelongsProject: onlyoffice-demo
 * @BelongsPackage: com.oo.onlyoffice.config
 * @Author: TongHui
 * @CreateTime: 2023-08-01 16:17
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
//@Configuration
public class CreateBean {


//    @Bean
    public OnlyProperties getOnlyProperties(){
        OnlyProperties onlyProperties = new OnlyProperties();
        JSONObject map = LoadConfigUtil.getMap();
        onlyProperties.setCallBackUrl(map.getStr("call-back-url"));
        onlyProperties.setDocService(map.getStr("doc-service"));
        onlyProperties.setDownloadFile(map.getStr("download-file"));
        onlyProperties.setHistNum(map.getInt("hist-num"));
        onlyProperties.setSecret(map.getStr("secret"));
        onlyProperties.setTimeout(map.getInt("timeout"));
        onlyProperties.setLocalhostAddress(map.getStr("localhost-address"));
        onlyProperties.setMaxSize(map.getLong("max-size"));
        return onlyProperties;
    }

//    @Bean
    public Cache getCache(RedisTemplate<String, Object> redisTemplate){
        JSONObject map = LoadConfigUtil.getMap();
        String c = map.getStr("cache");
        if (null != c && c.equals("redis")){
            return new RedisCache(redisTemplate);
        }

        return new InsideCache();
    }

    /**
     * RedisTemplate配置
     * @param lettuceConnectionFactory
     * @return
     */
//    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        log.info(" --- redis config init --- ");
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer jacksonSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }


}
