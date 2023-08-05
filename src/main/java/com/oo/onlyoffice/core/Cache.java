package com.oo.onlyoffice.core;

public interface Cache {

    Object get(String key);
    void set(String key,Object value,long time);
    void set(String key,Object value);
    boolean hasKey(String key);
    void remove(String key);
}
