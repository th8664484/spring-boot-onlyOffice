package com.oo.onlyoffice.tools;

import com.oo.onlyoffice.dto.edit.FileUser;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @BelongsProject: jeecg-boot-parent
 * @BelongsPackage: com.waysoft.tools
 * @Author: TongHui
 * @CreateTime: 2023-02-06 09:12
 * @Description: 存储当前登录的用户信息
 * @Version: 1.0
 */
@Slf4j
public class SecurityUtils implements Serializable {

    private static ThreadLocal<Object> loginEntityThreadLocal=new ThreadLocal<>();
    /**
     * 获取当前线程绑定的用户登录对象
     *
     * @return
     */
    public static FileUser getUserSession() {
        log.info(Thread.currentThread().getName()+"获取登录用户信息");
        return (FileUser)loginEntityThreadLocal.get();
    }


    public static Object getObjectSession() {
        return loginEntityThreadLocal.get();
    }

    /**
     * 将用户登录对象绑定到当前线程
     *
     * @param loginEntity
     */
    public static void setUserSession(Object loginEntity) {
        log.info(Thread.currentThread().getName()+"存入登录用户信息");
        loginEntityThreadLocal.set(loginEntity);
    }

    /**
     * 将用户登录对象从当前线程销毁
     */
    public static void removeUserSession() {
        log.info(Thread.currentThread().getName()+"清除用户信息");
        loginEntityThreadLocal.remove();
    }
}
