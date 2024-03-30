package com.example.onlyoffice_demo.service;

import com.office.config.oo.OnlyProperties;
import com.office.core.SaveFileProcessor;
import com.example.onlyoffice_demo.entity.OnFile;
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

//    @Autowired
    private OnlyProperties onlyProperties;
    @Autowired
    private OnFileService onFileService;


    @Override
    public void saveBeforeInitialization(Map<String, Object> map, byte[] bytes, String fileExtension) throws Exception {

    }

    @Override
    public Map<String, Object> save(Map<String, Object> map, byte[] file, byte[] changes, String key){
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
    public void saveAfterInitialization(Map<String, Object> map, byte[] bytes, String fileExtension) throws Exception {

    }


}
