package com.oo.onlyoffice.core;

import java.util.Map;

/**
 * 保存文件处理执行的方法
 */
public interface SaveFileProcessor {
    /**
     * 保存文件前进行自定义处理
     * @param bytes
     * @param fileExtension 文件后缀
     * @return
     * @throws Exception
     */
     void saveBeforeInitialization(Map<String, Object> map,byte[] bytes,String fileExtension) throws Exception;

    /**
     *
     * @param map     文件元信息
     * @param file    文件
     * @param changes 文件变动信息
     * @return 返回新的文件信息
     */
    Map<String, Object> save(Map<String, Object> map,byte[] file, byte[] changes,String key);

    /**
     * 保存文件后进行自定义处理
     * @param bytes
     * @param fileExtension 文件后缀
     * @return
     * @throws Exception
     */
     void saveAfterInitialization(Map<String, Object> map,byte[] bytes,String fileExtension) throws Exception;
}
