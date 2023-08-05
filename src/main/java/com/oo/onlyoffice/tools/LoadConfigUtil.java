package com.oo.onlyoffice.tools;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.oo.onlyoffice.dto.Plugins;
import com.oo.onlyoffice.dto.document.DocumentPermission;
import com.oo.onlyoffice.dto.edit.FileCustomization;
import com.oo.onlyoffice.dto.edit.FileEmbedded;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Iterator;

/**
 * @BelongsProject: onlyoffice-demo
 * @BelongsPackage: com.oo.onlyoffice.tools
 * @Author: TongHui
 * @CreateTime: 2023-07-30 18:30
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
public class LoadConfigUtil {
    private static JSONObject map = new JSONObject();
    private static DocumentPermission viewPermission = new DocumentPermission();
    private static DocumentPermission editPermission = new DocumentPermission();
    private static FileCustomization customization = new FileCustomization();
    private static FileEmbedded embedded = new FileEmbedded();
    private static Plugins plugins = new Plugins();

    private static String YML_NAME = "onlyOffice.yml";

    static {
            loadYml();
    }

    private static void loadYml(){
        log.info("--------加载【onlyOffice.yml】配置文件-------");
        InputStream inputStream = LoadConfigUtil.class.getClassLoader().getResourceAsStream(YML_NAME);
        //解析配置文件
        map = new Yaml().loadAs(inputStream, JSONObject.class);
        JSONObject oo = map.getJSONObject("oo");
        JSONObject document = oo.getJSONObject("document");
        if (null != document || null != document.getJSONObject("permissions")){
            JSONObject permissions =  document.getJSONObject("permissions");
            if (null != permissions.getJSONObject("edit")){
                editPermission = JSONUtil.toBean(permissions.getJSONObject("edit").toString(),DocumentPermission.class);
                editPermission.setEdit(true);
            }
            if (null != permissions.getJSONObject("view")){
                viewPermission = JSONUtil.toBean(permissions.getJSONObject("view").toString(),DocumentPermission.class);
                viewPermission.setEdit(false);
            }
        }
        JSONObject editor = oo.getJSONObject("editor");
        if (null != editor){
            customization = JSONUtil.toBean(editor.getJSONObject("customization").toString(),FileCustomization.class);
        }

        JSONObject embeddedJson = editor.getJSONObject("embedded");
        if (null != embeddedJson){
            embedded = JSONUtil.toBean(embeddedJson.toString(),FileEmbedded.class);
        }

        JSONObject pluginsJson = editor.getJSONObject("plugins");
        if (null != pluginsJson){
            plugins = JSONUtil.toBean(pluginsJson.toString(),Plugins.class);
        }
        log.info("--------【onlyOffice.yml】配置文件加载完成-------");
    }

    public static DocumentPermission getEditPermission() {
        return editPermission;
    }

    public static DocumentPermission getViewPermission() {
        return viewPermission;
    }

    public static FileCustomization getCustomization() {
        return customization;
    }

    public static FileEmbedded getEmbedded() {
        return embedded;
    }

    public static Plugins getPlugins() {
        return plugins;
    }

    public static JSONObject getConvert(){
        JSONObject oo = map.getJSONObject("oo");
        JSONObject convert = oo.getJSONObject("convert");
        return convert;
    }

    public static JSONObject getMap(){
        return map.getJSONObject("oo");
    }


    public static void main(String[] args) {

        JSONObject convert = LoadConfigUtil.getConvert();
        System.out.println(convert.toString());
    }
}
