package com.oo.onlyoffice.dto.edit;

import lombok.Data;

import java.io.Serializable;

/**
 * 使用文件的用户信息
 */
@Data
public class FileUser implements Serializable {

    /**
     * 用户唯一标识
     */
    private String id;

    /**
     * 用户 全称
     */
    private String name;

    /**
     * 组
     */
    private String[] group;
}
