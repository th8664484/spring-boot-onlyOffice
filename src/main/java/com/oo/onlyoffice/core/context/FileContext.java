package com.oo.onlyoffice.core.context;

import com.oo.onlyoffice.core.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储文件信息
 */
@Component
public class FileContext {

    public final Map<String, FileHandler> tempMaps = new ConcurrentHashMap<>();



    public static Map<String, FileMetadata> keyUrlInfo = new HashMap<>();

    @Autowired
    public FileContext(Map<String, FileHandler> tempMap) {
        this.tempMaps.clear();
        tempMap.forEach((k, v) -> this.tempMaps.put(v.getHandlerName(), v));
    }

    /**
     * 获取对应的执行
     *
     * @param handler
     * @return 结果
     */
    public FileHandler getHandler(String handler) {
        return tempMaps.get(handler);
    }

    /**
     * 获取对应的 处理方式
     *
     * @param key key
     * @return 结果
     */
    public FileHandler getHandlerByKey(String key) {
        for (FileHandler tempFileHandler : tempMaps.values()) {
            FileMetadata tempFileInfo = keyUrlInfo.get(tempFileHandler.getHandlerName() + key);
            if (Objects.nonNull(tempFileInfo)) {
                return tempFileHandler;
            }
        }
        return null;
    }

    public FileMetadata getFileInfo(String key) {
        for (FileHandler fileHandler : tempMaps.values()) {
            FileMetadata fileInfo = keyUrlInfo.get(fileHandler.getHandlerName() + key);
            if (Objects.nonNull(fileInfo)) {
                return fileInfo;
            }
        }
        return null;
    }

}
