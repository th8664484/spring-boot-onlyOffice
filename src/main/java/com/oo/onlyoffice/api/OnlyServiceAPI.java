package com.oo.onlyoffice.api;

import cn.hutool.json.JSONObject;
import com.oo.onlyoffice.core.context.FileMetadata;


import java.util.Map;


public interface OnlyServiceAPI {
    /**
     * 打开only office
     *
     * @param map                  文件数据信息
     * @param map{
     *           必填 fileId
     *           必填 fileName
     *           必填 fileType
     *           必填 fileSize
     *           可用携带其它值
     * }
     * @param mode                 打开方式  edit/view
     * @param collaborativeEditing 是否协同编辑
     * @return 配置信息
     */
    Map openDocument(Map<String, Object> map, String mode, boolean collaborativeEditing);




    /**
     * 处理保存结果
     *
     * @param jsonObject 信息
     */
    void handlerStatus(JSONObject jsonObject) throws Exception;


    /**
     * 删除临时文件
     *
     * @param jsonObject
     */
    void removeTempFile(JSONObject jsonObject);

    /**
     * 获取文件元数据
     *
     * @param key
     * @return
     */
    FileMetadata getTempFile(String key);

    /**
     * 获取历史数量上限
     *
     * @return
     */
    Integer getHistNum();

    /**
     * 获取打开文档时的唯一key
     *
     * @param id
     * @return
     */
    String getKey(String id);

    /**
     * 当文件没有人使用时，清空文件信息
     */
    void close(JSONObject jsonObject);

    /**
     * redis中存放文件使用人数
     *
     * @param key
     * @param users
     */
    int iskey(String key, Integer users);

    /**
     * 获取文档是使用人数
     * @return
     */
    int getUserNum(String key);

    /**
     * 获取文件id
     * @return
     */
    String getFileId(String key);

    String getCommandServiceUrl();

    Integer getTimeout();

    String downloadFileRequestPath();

    /**
     * 文件保存
     */
    String save(String key, String userId);

    /**
     * 文件转换
     * @param filetype   文件类型
     * @param fileId     文件id
     * @param outputtype 转换类型
     * @param title      转换后的名称
     * @param password    文档密码
     * @return 转换后的文件下载地址
     */
    String converted(String filetype, String fileId, String outputtype,  String title,String password);
}
