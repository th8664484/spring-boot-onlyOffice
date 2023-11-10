package com.oo.onlyoffice.core.context;

import lombok.Builder;
import lombok.Data;

import java.util.Map;


/**
 * 文件元数据信息
 */
@Data
@Builder(toBuilder = true)
public class FileMetadata {
    /**
     * 文件可访问的url
     */
    private String url;
    /**
     * 文件标示符 最大长度为10位
     */
    private String key;
    /**
     * 文件名称  源文件名
     */
    private String oldName;

    private String fileType;


    /**
     * 历史版本路径
     */
    private String histVerPath;


    /**
     * 文件信息
     */
    private Map<String,Object> fileInfo;

    /**
     * 打开时间
     */

    private long openTime;

}
