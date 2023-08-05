package com.oo.onlyoffice.core;


import com.oo.onlyoffice.vo.FileConfig;

public interface OnlyOfficeConfigFactory {


    /**
     * 文件初始化onlyOffice必须信息
     *
     * @param fileUrl  文件地址
     * @param key      文件唯一标识
     * @param fileName 文件名称
     * @return onlyOffice必须信息
     */
    FileConfig buildInitConfig(String fileUrl, String mode, String key, String fileName);
}
