package com.oo.onlyoffice.core.handler;

import cn.hutool.core.util.IdUtil;
import com.oo.onlyoffice.config.OnlyProperties;
import com.oo.onlyoffice.core.Cache;
import com.oo.onlyoffice.core.context.FileContext;
import com.oo.onlyoffice.core.context.FileMetadata;
import com.oo.onlyoffice.dto.edit.FileUser;
import com.oo.onlyoffice.core.FileHandler;
import com.oo.onlyoffice.tools.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 文件处理方式
 *
 * getID_+key                   存储的文件id
 * collaborativeEditing_+文件id  存储的 key  协同编辑使用同一个 key
 * userID+文件id                 存储的 key
 */
@Component
@Slf4j
public class FileHandlerImpl implements FileHandler {


//    @Autowired
    private OnlyProperties onlyProperties;
//    @Autowired
    private Cache cache;

    private int time = 60*30;



    @Override
    public String getHandlerName() {
        return "onlyOffice-";
    }


    /**
     * @param map
     * @param collaborativeEditing
     * @return
     * @throws Exception
     */
    public FileMetadata handlerFile(Map<String, Object> map, boolean collaborativeEditing) throws Exception {

        try {
            String id = (String) map.get("fileId");

            log.info("源文件存储的ID" + id);
            // 唯一标示符
            /**
             *  协同编辑时用的同一个key，判断是否协同，是：查找缓存中有无key，否：新生成key
             * */
            String key = "";
            String mode = (String) map.get("mode");
            if (mode.equals("edit")) { //编辑模式
                if (collaborativeEditing) {
                    if (cache.get("collaborativeEditing_" + id) != null) {
                        key = (String) cache.get("collaborativeEditing_" + id);
                    } else {
                        key = IdUtil.simpleUUID();
                        cache.set("collaborativeEditing_" + id, key);
                        cache.set("getID_" + key, id);
                    }
                } else {
                    key = IdUtil.simpleUUID();
                    FileUser user = SecurityUtils.getUserSession();
                    cache.set(user.getId() + "_" + id, key);
                    cache.set("getID_" + key, id);
                }
            } else {//查看模式
                key = id;
            }


            Optional<FileMetadata> tempFileInfoOptional = this.getTempFile(key);
            if (tempFileInfoOptional.isPresent()) {
                return tempFileInfoOptional.get();
            }

            //  生成临时可访问文件的url
            String tempUrl = getFileUri(id);

            FileMetadata tempFileInfo = FileMetadata.builder()
                    // 可访问路径
                    .url(tempUrl)
                    // 原来的文件名
                    .oldName((String) map.get("fileName"))
                    .fileType((String) map.get("fileType"))
                    .fileInfo(map)
                    // 唯一标识
                    .key(key)
                    .openTime(new Date().getTime())
                    .build();
            if (mode.equals("edit")) {
                FileContext.keyUrlInfo.put(getHandlerName() + key, tempFileInfo);
            }
            return tempFileInfo;
        } catch (Exception e) {
            log.error("生成临时文件失败", e);
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public void removeTempFile(String key) {
        Optional<FileMetadata> tempFileInfo = this.getTempFile(key);
        if (tempFileInfo.isPresent()) {
            FileContext.keyUrlInfo.remove(getHandlerName() + key);
        }
    }


    /**
     * 判断是否存在临时文件信息
     *
     * @param key
     * @return
     */
    public Optional<FileMetadata> getTempFile(String key) {
        FileMetadata tempFileInfo = FileContext.keyUrlInfo.get(getHandlerName() + key);
        return Optional.ofNullable(tempFileInfo);
    }



    /**
     * 获取文件可访问的地址
     *
     * @param id
     * @return 地址
     */
    private String getFileUri(String id) {
        if (onlyProperties.getDownloadFile().endsWith("/")) {
            return onlyProperties.getDownloadFile() + id ;
        }
        return onlyProperties.getDownloadFile() + "/"+ id;
    }
}
