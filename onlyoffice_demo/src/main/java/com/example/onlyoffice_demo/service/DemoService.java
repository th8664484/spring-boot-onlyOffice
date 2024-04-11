package com.example.onlyoffice_demo.service;

import com.office.config.oo.OnlyProperties;
import com.office.core.CommonConfig;
import com.office.core.SaveFileProcessor;
import com.example.onlyoffice_demo.entity.OnFile;
import com.office.office.oo.OnlyOfficeAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @BelongsProject: onlyoffice-demo
 * @BelongsPackage: com.example.onlyoffice_demo.service
 * @Author: TongHui
 * @CreateTime: 2023-08-01 16:00
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class DemoService implements SaveFileProcessor {

    @Autowired
    private OnFileService onFileService;


    @Override
    public void saveBeforeInitialization(Map<String, Object> map, byte[] bytes, String fileExtension,OnlyOfficeAPI onlyOfficeAPI) throws Exception {

    }

    @Override
    public Map<String, Object> save(Map<String, Object> map, byte[] file, byte[] changes, String key,OnlyOfficeAPI onlyOfficeAPI){

        //文件id
        String id = onlyOfficeAPI.getFileId(key);

        //判断是否是最后一人进行保存
        int users = onlyOfficeAPI.getUserNum(key);
        if (users > 1) {
            return map;
        }

        //历史版本最大个数 获取当前文件的历史版本数量
        Integer histNum = onlyOfficeAPI.getHistNum();
        /**
         * 如果有需要保存历史记录 可以进行相关操作
         */
        if (null != histNum){

        }

        String fileId = "";
        try {
          fileId =   onFileService.saveFile(file,map.get("fileType").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String version = map.get("version").toString();

        String[] split = version.split("\\.");
        version = split[0]+"."+split[1]+"."+ (Integer.valueOf(split[2])+1);

        OnFile onFile = new OnFile();
        onFile.setFileId(fileId);
        onFile.setFileName(map.get("fileName").toString());
        onFile.setVersion(version);
        onFile.setFileSize((long) file.length);
        onFile.setFileType(map.get("fileType").toString());
        onFileService.save(onFile);

        map.put("version", version);
        return map;
    }

    @Override
    public void saveAfterInitialization(Map<String, Object> map, byte[] bytes, String fileExtension,OnlyOfficeAPI onlyOfficeAPI) throws Exception {

    }


}
