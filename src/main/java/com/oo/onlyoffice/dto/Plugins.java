package com.oo.onlyoffice.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @BelongsProject: self-study
 * @BelongsPackage: com.project.dto
 * @Author: TongHui
 * @CreateTime: 2022-12-23 16:23
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Data
public class Plugins {

    /** 插件的 guid  asc.{4FF5B2DB-BDDA-CC2A-5A36-0087719EB455} */
    private String[] autostart;
    /** 插件地址  服务器地址+guid+/config.json  这的guid没有前缀 {4FF5B2DB-BDDA-CC2A-5A36-0087719EB455}*/
    private String[] pluginsData;


}
