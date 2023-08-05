package com.oo.onlyoffice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 配置信息
 */
@Data
public class OnlyProperties {


    private String localhostAddress;

    /**
     * 回调地址 不包含http表示本机地址
     */

    private String callBackUrl ;

    /**
     * only office服务路径
     * 必须存在
     */

    private String docService ;
    /**
     * 下载文件请求接口
     * */

    private String downloadFile;

    /**超时时间*/

    private Integer timeout ;


    /**
     * 历史文件数量
     */

    private Integer histNum ;

    /**
     * JWT令牌
     */

    private String secret ;


    /**
     * 打开文件最大MB
     * */
    private Long maxSize;


    public final String DOC_API_URL = "/web-apps/apps/api/documents/api.js";
    public final String CONVERTER = "/ConvertService.ashx";
    public final String SAVE = "/coauthoring/CommandService.ashx";


    public void setMaxSize(Long maxSize) {
        if (null == maxSize){
            this.maxSize = Long.valueOf(20*1024*1024);
        }else {
            this.maxSize = maxSize*1024*1024;
        }
    }
}
