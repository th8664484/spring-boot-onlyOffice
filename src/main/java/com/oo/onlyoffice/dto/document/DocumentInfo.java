package com.oo.onlyoffice.dto.document;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * file文档部分
 */
@Data
@Builder(toBuilder = true)
public class DocumentInfo implements Serializable {

    /**
     * 定义收藏夹图标的突出显示状态。 当用户单击该图标时，将调用onMetaChange事件
     */
    private String favorite;

    /**
     * 创建时间（格式化后数据）
     */
    private String owner;
    /**
     * 创建时间（格式化后数据）
     */
    private String created;

    /**
     * 存储文件夹可以为空
     */
    private String folder;

    /**
     * 分享
     * Defines the settings which will allow to share the document with other users:
     *   permissions - the access rights for the user with the name above. Can be Full Access, Read Only or Deny Access
     *     type: string
     *     example: "Full Access"
     *   user - the name of the user the document will be shared with
     *     type: string
     *     example: "John Smith".
     */
    private List<SharingSettings> sharingSettings;

}
