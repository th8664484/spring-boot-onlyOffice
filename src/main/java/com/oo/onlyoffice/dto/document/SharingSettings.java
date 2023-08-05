package com.oo.onlyoffice.dto.document;

import lombok.Data;

/**
 * @BelongsProject: leaf-onlyoffice
 * @BelongsPackage: com.ideayp.leaf.onlyoffice.dto.document
 * @Author: TongHui
 * @CreateTime: 2022-11-09 13:53
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class SharingSettings {

    private Boolean isLink; //将用户图标更改为链接图标
    private String[] permissions; //完全访问，只读或拒绝访问  Full Access, Read Only , Deny Access
    private String user; //共享文档的用户的名称

}
