package com.oo.onlyoffice.dto.edit;

import lombok.Data;

import java.io.Serializable;

/**
 * 最近打开
 */
@Data
public class FileRecent implements Serializable {

    /**
     * 文件夹
     */
    private String folder;

    /**
     * 名称
     */
    private String title;

    /**
     * url 绝对路径
     */
    private String url;

}
