package com.oo.onlyoffice.core;



import com.oo.onlyoffice.core.context.FileMetadata;

import java.util.Map;
import java.util.Optional;


public interface FileHandler {
    /**
     * 处理名称
     *
     * @return 名称
     */
    String getHandlerName();


    /**
     *
     * @param map{
     *           必填 fileId
     *           必填 fileName
     *           必填 fileType
     *           必填 fileSize
     *           可用携带其它值
     * }
     * @param collaborativeEditing
     * @return
     * @throws Exception
     */
    FileMetadata handlerFile(Map<String, Object> map, boolean collaborativeEditing) throws Exception;



    /**
     * 移除临时文件信息
     *
     * @param key key
     */
    void removeTempFile(String key);

    /**
     * 获取临时文件信息
     *
     * @param key key
     */
     Optional<FileMetadata> getTempFile(String key);
}
