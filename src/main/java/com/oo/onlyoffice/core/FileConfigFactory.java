package com.oo.onlyoffice.core;

import cn.hutool.core.date.DateUtil;
import com.oo.onlyoffice.core.OnlyOfficeConfigFactory;
import com.oo.onlyoffice.dto.DocumentConfig;
import com.oo.onlyoffice.dto.EditorConfig;
import com.oo.onlyoffice.dto.Plugins;
import com.oo.onlyoffice.tools.*;
import com.oo.onlyoffice.config.OnlyProperties;
import com.oo.onlyoffice.dto.document.DocumentInfo;
import com.oo.onlyoffice.dto.document.DocumentPermission;
import com.oo.onlyoffice.dto.document.SharingSettings;
import com.oo.onlyoffice.dto.edit.FileCustomization;
import com.oo.onlyoffice.dto.edit.FileUser;
import com.oo.onlyoffice.vo.FileConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @BelongsProject: leaf-onlyoffice
 * @BelongsPackage: com.ideayp.leaf.onlyoffice.dto
 * @Author: TongHui
 * @CreateTime: 2022-11-16 17:00
 * @Description: TODO
 * @Version: 1.0
 */
@Component
public class FileConfigFactory implements OnlyOfficeConfigFactory {

    @Autowired
    private OnlyProperties onlyProperties;

    /**
     * 初始化 only office 最基础的信息必要数据
     *
     * @param fileUrl  可访问的url路径
     * @param mode     打开方式
     * @param key      唯一标示符 20个字符以内
     * @param fileName 文件名称
     * @return 配置信息
     */
    @Override
    public FileConfig buildInitConfig(String fileUrl, String mode, String key, String fileName) {

        Map<String, Object> map = new HashMap<>();

        FileConfig fileConfigDTO = new FileConfig();
        // 1、文档类型
        fileConfigDTO.setDocumentType(OnlyOfficeUtil.getDocumentType(fileName));
        map.put("documentType", fileConfigDTO.getDocumentType());
        map.put("type", fileConfigDTO.getType());
        // 2、onlyoffice的 api位置
        fileConfigDTO.setDocServiceApiUrl(onlyProperties.getDocService() + onlyProperties.getDOC_API_URL());

        // ==========用户============
        FileUser user = getUser();
        // ========文档类型=============
        String typePart = FileUtil.getFileExtension(fileName);
        DocumentConfig fileDocument = DocumentConfig.builder()
                // 文件名
                .title(fileName)
                // 扩展名
                .fileType(typePart)
                // 可访问的url
                .url(fileUrl)
                // 唯一有标示符
                .key(key)
                .info(getDocumentInfo(user))
                .permissions(buildPermission(mode))
                .build();
        fileConfigDTO.setDocument(fileDocument);
        map.put("document", fileDocument);
        // ==========编辑配置===============
        String callBackUrl = onlyProperties.getLocalhostAddress() + onlyProperties.getCallBackUrl();

        EditorConfig editorConfig = new EditorConfig(callBackUrl, mode);
        editorConfig.setFileCustomization(getFileCustomization());
        editorConfig.setFileUser(user);
        editorConfig.setPlugins(getPlugins());


        fileConfigDTO.setEditorConfig(editorConfig);
        map.put("editorConfig", editorConfig);


        if (null != onlyProperties.getSecret()){
            String token =  JWTUtil.createToken(map,onlyProperties.getSecret());
            fileConfigDTO.setToken(token);
        }

        return fileConfigDTO;
    }


    private DocumentInfo getDocumentInfo(FileUser user) {
        SharingSettings sharingSettings = new SharingSettings();
        sharingSettings.setPermissions(new String[]{"Full Access"});
        sharingSettings.setUser(user.getName());
        sharingSettings.setIsLink(true);
        DocumentInfo info = DocumentInfo.builder()
                .created(DateUtil.formatDateTime(new Date()))
                .sharingSettings(Collections.singletonList(sharingSettings)).build();
        return info;
    }

    private DocumentPermission buildPermission(String mode) {
        if (mode.equals("view")) {
           return LoadConfigUtil.getViewPermission();
        }
        return LoadConfigUtil.getEditPermission();
    }

    private FileCustomization getFileCustomization() {
        return LoadConfigUtil.getCustomization();
    }


    private Plugins getPlugins() {
        return LoadConfigUtil.getPlugins();
    }


    private FileUser getUser() {
        FileUser fileUser =  SecurityUtils.getUserSession();
        return fileUser;
    }
}
