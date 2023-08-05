package com.oo.demo.service;

import cn.hutool.core.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @BelongsProject: onlyoffice-demo
 * @BelongsPackage: com.oo.demo.service
 * @Author: TongHui
 * @CreateTime: 2023-08-05 17:14
 * @Description: TODO
 * @Version: 1.0
 */
public class TempUser {
    private static Logger logger = LoggerFactory.getLogger(TempUser.class);
    private static List<Map<String,String>> cache1 = new ArrayList<>();
    private static Integer index;

    static {
        for (int i = 0; i < 10; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("userId", IdUtil.simpleUUID().substring(0,6));
            map.put("userName","TongHui"+map.get("userId"));
            cache1.add(map);
        }
        logger.info("用户初始完毕");
        Random random = new Random();
        index = random.nextInt(10);
        logger.info("获取用户完毕"+index);
    }
    public static String getUserName(){
        return cache1.get(index).get("userName");
    }
    public static String getUserId(){
        return cache1.get(index).get("userId");
    }
}
