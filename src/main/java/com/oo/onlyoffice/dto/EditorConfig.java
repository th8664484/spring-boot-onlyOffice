package com.oo.onlyoffice.dto;

import com.oo.onlyoffice.dto.edit.FileCustomization;
import com.oo.onlyoffice.dto.edit.FileEmbedded;
import com.oo.onlyoffice.dto.edit.FileRecent;
import com.oo.onlyoffice.dto.edit.FileUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑配置
 */
@Data
@Accessors(chain = true)
public class EditorConfig implements Serializable {

    /**
     * 回调地址
     * 指定文档存储服务的绝对URL（必须由在自己的服务器上使用ONLYOFFICE Document Server的软件集成器实现）。
     * 必填*
     */
    private String callbackUrl;

    /**
     * 定义文档创建后的绝对URL，并在创建后可用。如果没有指定，将没有创建按钮
     */
    private String createUrl;


    /**
     * 语言
     * 定义编辑器接口语言（如果存在除英语之外的其他语言）。
     * 使用两个字母（DE、RU、IT等）或四个字母（EN美国、FR FR等）来设置语言代码。不填默认值是"EN US"。
     */
    private String lang = "zh-CN";


    /**
     * 定义编辑器打开模式。
     * 可以是打开用于查看的文档的视图，也可以是在允许对文档数据应用更改的编辑模式下打开文档的编辑。
     * 默认值是edit
     * view 视图
     * edit 编辑
     */
    private String mode;

    /**
     * 最近打开历史
     */
    private List<FileRecent> recent;

    /**
     * 用户信息
     */
    private FileUser user;

    /**
     * 自定义信息
     */
    private FileCustomization customization;

    /**
     *共同编辑
     */
    private Object coEditing;

    private FileEmbedded embedded;

    /**
     * 请查看官方文档
     * 自定义插件
     * https://api.onlyoffice.com/editors/config/editor/plugins
     */
    private Plugins plugins;

    public EditorConfig(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    public EditorConfig(String callbackUrl,String mode) {
        this.callbackUrl = callbackUrl;
        this.mode = mode;
    }

    public void setFileCustomization(FileCustomization customization){
        this.customization = customization;
    }


    public void setFileUser(FileUser user){
        this.user = user;
    }
}
