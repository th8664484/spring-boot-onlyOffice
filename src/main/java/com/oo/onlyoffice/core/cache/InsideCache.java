package com.oo.onlyoffice.core.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.oo.onlyoffice.core.Cache;
import lombok.extern.slf4j.Slf4j;

/**
 * @BelongsProject: onlyoffice-demo
 * @BelongsPackage: com.oo.onlyoffice.tools
 * @Author: TongHui
 * @CreateTime: 2023-07-30 17:11
 * @Description: 内部缓存
 * @Version: 1.0
 */
@Slf4j
public class InsideCache implements Cache {

    private  long timeout = 60;

    //时间单位 秒
    private  long timeUnit = 1000;

    //创建缓存，默认 1分钟 过期
    private  TimedCache<String, Object> cache = CacheUtil.newTimedCache(timeout*timeUnit);


    public InsideCache(){
        cache.schedulePrune(timeout*timeUnit);
    }

    public  Object get(String key){
        return cache.get(key);
    }

    public  boolean hasKey(String key){
        return cache.get(key) == null ? false : true;
    }

    /**
     *
     * @param key
     * @param value
     * @param time  时间 单位是秒
     */
    public  void set(String key,Object value,long time){
        if (time < timeUnit){
            time = timeout;
        }
        log.info("key:{},value:{},size:{}",key,value,cache.size());
        cache.put(key, value, time*timeUnit);
    }

    @Override
    public void set(String key, Object value) {
        cache.put(key, value);
    }

    public  void remove(String key){
        cache.remove(key);
    }


}
