package com.example.onlyoffice_demo.config;

import com.office.core.Cache;
import org.springframework.stereotype.Component;

/**
 * 项目名： spring-boot-onlyOffice
 * 包路径： com.example.onlyoffice_demo.config
 * 作者：   TongHui
 * 创建时间: 2024-04-26 13:14
 * 描述: TODO
 * 版本: 1.0
 */
@Component
public class MyCache implements Cache {


    @Override
    public String getCacheName() {
        return this.getClass().getName();
    }

    @Override
    public Object get(String key) {
        return "abc";
    }

    @Override
    public void set(String key, Object value, long time) {

    }

    @Override
    public void set(String key, Object value) {

    }

    @Override
    public boolean hasKey(String key) {
        return false;
    }

    @Override
    public void remove(String key) {

    }
}
