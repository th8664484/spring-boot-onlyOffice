package com.oo.onlyoffice.dto.edit;

import lombok.Data;

import java.io.Serializable;

/**
*
*@Author TongHui
*@Date 2022/11/16
*@ClassName FileCustomer
*@Version 1.0
*/
@Data
public class FileCustomer implements Serializable {

    /**
     * 地址
     */
    private String address;

    /**
     * 介绍信息
     */
    private String info;

    /**
     * logo地址
     * 将显示在“关于”页上的图像标志的路径（该文件没有特别推荐，但如果是具有透明背景的.png格式会更好）。
     * 图像必须具有以下尺寸：432×70
     */
    private String logo;

    /**
     * 联系邮箱
     */
    private String mail;

    /**
     * 名称
     */
    private String name;

    /**
     * 网址
     */
    private String www;


}
