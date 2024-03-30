package com.example.wps_demo.service;

import cn.hutool.core.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @BelongsProject: onlyoffice-demo
 * @BelongsPackage: com.example.onlyoffice_demo.service
 * @Author: TongHui
 * @CreateTime: 2023-08-05 17:14
 * @Description: TODO
 * @Version: 1.0
 */
public class TempUser {
    private static Logger logger = LoggerFactory.getLogger(TempUser.class);
    private static List<TempUser> cache1 = new ArrayList<>();
    private String id;
    private String name;

    static {
        for (int i = 0; i < 10; i++) {
            String userId = IdUtil.simpleUUID().substring(0,6);
            cache1.add(new TempUser(userId,"TongHui"+userId));
        }
        logger.info("用户初始完毕");
    }

    public  TempUser(String id ,String name){
        this.id = id;
        this.name = name;
    }

    public static TempUser getUser(){
        Random random = new Random();
        int index = random.nextInt(10);
        return cache1.get(index);
    }

    public String getUserId() {
        return id;
    }

    public String getUserName() {
        return name;
    }
}
