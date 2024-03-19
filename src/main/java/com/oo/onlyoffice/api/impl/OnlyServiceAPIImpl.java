package com.oo.onlyoffice.api.impl;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.oo.onlyoffice.api.OnlyServiceAPI;
import com.oo.onlyoffice.config.OnlyProperties;
import com.oo.onlyoffice.core.Cache;
import com.oo.onlyoffice.core.SaveFileProcessor;
import com.oo.onlyoffice.core.context.FileMetadata;
import com.oo.onlyoffice.tools.DocumentKey;
import com.oo.onlyoffice.tools.JWTUtil;
import com.oo.onlyoffice.vo.FileConfig;
import com.oo.onlyoffice.core.OnlyOfficeConfigFactory;
import com.oo.onlyoffice.dto.convert.ConvertBody;
import com.oo.onlyoffice.dto.edit.FileUser;
import com.oo.onlyoffice.core.context.FileContext;
import com.oo.onlyoffice.core.FileHandler;
import com.oo.onlyoffice.tools.FileUtil;
import com.oo.onlyoffice.tools.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * only office 实现类
 */
@Service
@Slf4j
public class OnlyServiceAPIImpl implements OnlyServiceAPI {

//    @Autowired
    private OnlyOfficeConfigFactory onlyOfficeConfigFactory;
//    @Autowired
    private SaveFileProcessor saveFileProcessor;
//    @Autowired
    private OnlyProperties onlyProperties;
//    @Autowired
    private FileContext tempFileContext;
//    @Autowired
    private FileHandler fileHandler;
//    @Autowired
    private Cache cache;

    private String EDIT = "edit";
    private String VIEW = "view";


    /**
     * @param map                  文件信息
     * @param map{
     *           必填 fileId
     *           必填 fileName
     *           必填 fileType
     *           必填 fileSize
     *           可用携带其它信息
     * }
     * @param mode                 打开方式 edit/view
     * @param collaborativeEditing 是否协同编辑
     * @return
     */
    @Override
    public Map openDocument(Map<String, Object> map, String mode, boolean collaborativeEditing) {
        long fileSize = (long) map.get("fileSize");

        if (fileSize > onlyProperties.getMaxSize()) {
            // 添加操作日志
            throw new RuntimeException("文件超过【"+onlyProperties.getMaxSize()/1024/1024+"MB】无法打开");
        }

        if (EDIT.equals(mode)) {
            return documentEdit(map,collaborativeEditing);
        }
        if (VIEW.equals(mode)) {
            return documentView(map);
        }
        return null;
    }

    private Map documentEdit(Map<String, Object> map,boolean collaborativeEditing)  {
        FileConfig fileConfigDTO = openEditConfig(map, "edit", collaborativeEditing);
        String json = JSONUtil.toJsonStr(fileConfigDTO);

        Map<String, Object> config = JSONUtil.toBean(json, Map.class);


        return config;
    }


    private Map documentView(Map<String, Object> map)  {
        FileConfig fileConfigDTO = openEditConfig(map, "view", false);
        String json = JSONUtil.toJsonStr(fileConfigDTO);
        Map<String, Object> config = JSONUtil.toBean(json, Map.class);

        return config;
    }

    private FileConfig openEditConfig(Map<String, Object> map, String mode, boolean collaborativeEditing) {
        try {
            map.put("mode", mode);
            log.info("开始生成文件信息");
            //在编辑模式 生成临时文件，保存原文件信息
            FileMetadata tempFileInfo = fileHandler.handlerFile(map,collaborativeEditing) ;
            //生成配置文件 TODO: 控制文件权限
            log.info("开始生成编辑器配置信息");
            FileConfig fileConfigDTO = onlyOfficeConfigFactory.buildInitConfig(tempFileInfo.getUrl(), mode, tempFileInfo.getKey(), tempFileInfo.getOldName());
            log.info("生成编辑器配置信息结束");
            // TODO: 添加更多详细的自定义信息
            return fileConfigDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 获取规定历史文件数量
     *
     * @return
     */
    @Override
    public Integer getHistNum() {
        return onlyProperties.getHistNum();
    }


    /**
     * 保存文件地址
     *
     * @return
     */
    @Override
    public String getCommandServiceUrl() {
        return onlyProperties.getDocService() + onlyProperties.SAVE;
    }

    @Override
    public Integer getTimeout() {
        return onlyProperties.getTimeout();
    }

    @Override
    public String downloadFileRequestPath() {
        if (onlyProperties.getDownloadFile().endsWith("/")) {
            return onlyProperties.getDownloadFile();
        }
        return onlyProperties.getDownloadFile() + "/";
    }


    @Override
    public void handlerStatus(JSONObject jsonObject) throws Exception {
        log.info("开始下载编辑器文件");
        int status = jsonObject.getInt("status");
        log.info("status[{}]:{}", status, jsonObject);
        String key = (String) jsonObject.get("key");

        FileHandler tempFileHandler = tempFileContext.getHandlerByKey(key);
        if (Objects.nonNull(tempFileHandler)) {
            String url = jsonObject.getStr("url");
            String changesurl = jsonObject.getStr("changesurl");
            log.info("编辑后的文档下载路径url:" + url);
            log.info("文件变动信息文件url:" + changesurl);
            // 下载修改后文件
            byte[] fileByte = FileUtil.getFileByte(url);
            // 下载前一个文化和修改后文件的区别
            byte[] changes = FileUtil.getFileByte(changesurl);
            log.info("下载编辑器文件成功");

            log.info("保存文件前置处理");
            try {
                String fileExtension = FileUtil.getFileExtension(url);

                Optional<FileMetadata> tempFile = tempFileHandler.getTempFile(key);

                if (!tempFile.isPresent()){
                    throw new RuntimeException("文件元信息不存在");
                }


                saveFileProcessor.saveBeforeInitialization(tempFile.get().getFileInfo(),fileByte,fileExtension);

                // 保存文件
                Map<String, Object> map = saveFileProcessor.save(tempFile.get().getFileInfo(),fileByte,changes, key);

                saveFileProcessor.saveAfterInitialization(tempFile.get().getFileInfo(),fileByte,fileExtension);
                //更新内存文件信息
                tempFileContext.updateCacheFileInfo(key,map);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public void removeTempFile(JSONObject jsonObject) {
        String key = (String) jsonObject.get("key");
        FileHandler tempFileHandler = tempFileContext.getHandlerByKey(key);
        if (tempFileHandler != null) {
            log.info("删除临时文件信息缓存" + key);
            tempFileHandler.removeTempFile(key);
        }
    }

    /**
     * 临时文件信息
     *
     * @param key
     * @return
     */
    public FileMetadata getTempFile(String key) {
        FileMetadata tempFileInfo = tempFileContext.getFileInfo(key);
        if (tempFileInfo != null) {
            return tempFileInfo;
        }
        return null;
    }

    /**
     * 获取打开文档时的唯一key
     *
     * @param id 文件id
     * @return
     */
    public String getKey(String id) {
        FileUser user = SecurityUtils.getUserSession();

        if (cache.hasKey(user.getId() + "_" + id)) {
            return (String) cache.get(user.getId() + "_" + id);
        }

        return (String) cache.get("collaborativeEditing_" + id);
    }

    public void close(JSONObject jsonObject) {
        // 判断临时信息是否存在
        FileMetadata tempFile = getTempFile(jsonObject.getStr("key"));
        if (tempFile == null){
            return;
        }

        //判断 删除时间 是否大于 文件打开时间
        //如果 删除时间 小于 文件打开时间 不执行删除
        log.info("打开时间："+tempFile.getOpenTime()+"，回调时间："+ new Date().getTime());
        if (new Date().getTime() < tempFile.getOpenTime()){
            return;
        }
        // 减少文档的使用人数
        int i = iskey(jsonObject.getStr("key"), null);
        //如果没有人使用当前文档，清空临时信息
        if (i <= 0) {
            removeTempFile(jsonObject);
            String id = (String) cache.get("getID_" + jsonObject.getStr("key"));
            cache.remove("getID_" + id);
            cache.remove("collaborativeEditing_" + id);
            cache.remove(id);
        }
    }

    /**
     * redis中存放文件使用人数
     *
     * @param key
     * @param users
     */
    public int iskey(String key, Integer users) {
        int i = 0;
        if (users != null) {
            cache.set(key, users);
            i = users;
        } else {
            i = ((int)cache.get(key)) - 1;
            cache.set(key, (i>=0 ? i : 0));
        }
        log.info("[" + key + "]文档使用人数：" + i);
        return i;
    }

    @Override
    public int getUserNum(String key) {
        return  (int)(cache.get(key));
    }

    @Override
    public String getFileId(String key) {
        return (String) cache.get("getID_" + key);
    }

    /**
     * https://documentserver/coauthoring/CommandService.ashx
     * {
     * "c": "forcesave",
     * "key": "Khirz6zTPdfd7",
     * "userdata": "sample userdata"
     * }
     */
    public String save(String key, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("c", "forcesave");
        map.put("key", key);
        map.put("userdata", userId);
        if (null != onlyProperties.getSecret()){
            String token =  JWTUtil.createToken(map,onlyProperties.getSecret());
            map.put("token",token);
        }
        String bodyString = JSONUtil.toJsonStr(map);
        log.info("forcesave:"+bodyString);
        byte[] bodyByte = bodyString.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(onlyProperties.getDocService() + onlyProperties.SAVE);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setFixedLengthStreamingMode(bodyByte.length);
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(onlyProperties.getTimeout());

            connection.connect();

            OutputStream os = connection.getOutputStream();
            os.write(bodyByte);

            InputStream stream = connection.getInputStream();

            if (stream == null) {
                throw new Exception("Could not get an answer");
            }

            /**将流转为字符串*/
            String jsonString = FileUtil.ConvertStreamToString(stream);
            connection.disconnect();
            JSONObject jsonObj = JSONUtil.toBean(jsonString,JSONObject.class);
            log.debug(jsonObj.toString());
            Object error = jsonObj.get("error");
            String msg = "";
            if (error != null) {
                switch ((Integer) error) {
                    case 1:
                        msg = "缺少文档密钥或找不到具有此类密钥的文档";
                        break;
                    case 2:
                        msg = "回调网址不正确";
                        break;
                    case 3:
                        msg = "内部服务器错误";
                        break;
                    case 4:
                        msg = "在收到强制保存命令之前，未对文档应用任何更改";
                        break;
                    case 5:
                        msg = "命令不正确";
                        break;
                    case 6:
                        msg = "令牌无效";
                        break;
                    default:
                        msg = "保存成功";
                }
            }
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 文件转化 #简单请求参数示例:
     * @param filetype   文件类型
     * @param key        文件key
     * @param outputtype 输出类型
     * @param fileUrl    文件路径
     * @param title      输出文件名称
     * @param password   加密文档的密码
     * @return 文件路径
     */
    /**
     * //#请求参数示例 https://www.songbin.top/post_27457.html
     * {
     * "filetype": "xlsx",
     * "key": "Khirz6zTPdfd7",
     * "outputtype": "pdf",
     * "region": "en-US",
     * "spreadsheetLayout": {
     * "ignorePrintArea": true,
     * "orientation": "portrait",
     * "fitToWidth": 0,
     * "fitToHeight": 0,
     * "scale": 100,
     * "headings": false,
     * "gridLines": false,
     * "pageSize": {
     * "width": "210mm",
     * "height": "297mm"
     * },
     * "margins": {
     * "left": "17.8mm",
     * "right": "17.8mm",
     * "top": "19.1mm",
     * "bottom": "19.1mm"
     * }
     * },
     * "title": "Example Document Title.pdf",
     * "url": "https://example.com/url-to-example-spreadsheet.xlsx"
     * }
     */
    @Override
    public String converted(String filetype, String key, String outputtype, String title,String password) {
        log.debug("文件开始转化{}->{}", filetype, outputtype);

        String fileUrl =  onlyProperties.getDownloadFile()+key;

        ConvertBody body = new ConvertBody(filetype, DocumentKey.SnowflakeId(), outputtype, fileUrl, title,password);
        String bodyString = JSONUtil.toJsonStr(body);

        if (null != onlyProperties.getSecret()){
            String token =  JWTUtil.createToken(JSONUtil.toBean(bodyString,Map.class),onlyProperties.getSecret());
            body.setToken(token);
            bodyString = JSONUtil.toJsonStr(body);
        }

        log.debug(bodyString);
        byte[] bodyByte = bodyString.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(onlyProperties.getDocService() + onlyProperties.CONVERTER);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setFixedLengthStreamingMode(bodyByte.length);
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(onlyProperties.getTimeout());

            connection.connect();

            OutputStream os = connection.getOutputStream();
            os.write(bodyByte);

            InputStream stream = connection.getInputStream();

            if (stream == null) {
                throw new Exception("Could not get an answer");
            }

            /**将流转为字符串*/
            String jsonString = FileUtil.ConvertStreamToString(stream);

            connection.disconnect();

            JSONObject jsonObj = JSONUtil.toBean(jsonString,JSONObject.class);
            log.debug(jsonObj.toString());
            /**
             * {
             *     "endConvert":true，//转换是否完成
             *     "fileUrl"：“ https：//documentserver/ResourceService.ashx?filename=output.doc”，//转换后的文件地址
             *     "percent"：100//转换完成百分比 仅参数设置为异步时
             *  }
             */
            Object error = jsonObj.get("error");
            if (error != null) {
                ProcessConvertServiceResponceError((Integer) error);
            }

            /**检查转换是否完成，并将结果保存到一个变量中*/
            Boolean isEndConvert = (Boolean) jsonObj.get("endConvert");
            Long resultPercent = 0L;
            String responseUri = null;

            if (isEndConvert) {
                resultPercent = 100L;
                responseUri = (String) jsonObj.get("fileUrl");
                log.debug("文件转化完成{}->{}", filetype, outputtype);
            } else {
                resultPercent = (Long) jsonObj.get("percent");
                resultPercent = resultPercent >= 100l ? 99l : resultPercent;
            }

            return resultPercent >= 100L ? responseUri : "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 错误代码	描述
     * -1	未知错误。
     * -2	转换超时错误。
     * -3	转换错误。
     * -4	下载要转换的文档文件时出错。
     * -5	密码不正确。
     * -6	访问转换结果数据库时出错。
     * -7	输入错误。
     * -8	令牌无效。
     */
    private void ProcessConvertServiceResponceError(int errorCode) throws Exception {
        String errorMessage = "";
        String errorMessageTemplate = "Error occurred in the ConvertService: ";
        switch (errorCode) {
            case -8:
                errorMessage = errorMessageTemplate + "令牌无效";
                break;
            case -7:
                errorMessage = errorMessageTemplate + "输入错误";
                break;
            case -6:
                errorMessage = errorMessageTemplate + "访问转换结果数据库时出错";
                break;
            case -5:
                errorMessage = errorMessageTemplate + "密码不正确";
                break;
            case -4:
                errorMessage = errorMessageTemplate + "下载要转换的文档文件时出错";
                break;
            case -3:
                errorMessage = errorMessageTemplate + "转换错误";
                break;
            case -2:
                errorMessage = errorMessageTemplate + "转换超时错误";
                break;
            case -1:
                errorMessage = errorMessageTemplate + "未知错误";
                break;
            case 0:
                break;
            default:
                errorMessage = "ErrorCode = " + errorCode;
                break;
        }
        throw new Exception(errorMessage);
    }
}


