package com.example.wps_demo.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.wps_demo.entity.OnFile;
import com.office.config.wps.FileInfo;
import com.office.config.wps.FileWatermark;
import com.office.config.wps.UserAcl;
import com.office.config.wps.UserInfo;
import com.office.exception.OfficeException;
import com.office.office.wps.ConstantsWPS;
import com.office.office.wps.WPSOfficeAPI;
import com.office.office.wps.dto.OnnotifyMessage;
import com.office.tools.wps.UserPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目名： spring-boot-onlyOffice
 * 包路径： com.example.wps_demo.service
 * 作者：   TongHui
 * 创建时间: 2024-03-27 18:27
 * 描述: TODO
 * 版本: 1.0
 */
@Service
@Slf4j
public class WPSCallBackService {

    @Autowired
    private OnFileService onFileService;
    @Autowired
    private WPSOfficeAPI wpsOfficeAPI;


    //获取头像地址
    private String avatarUrl = "";

    /**
     * 获取文件元数据
     *    mode             操作类型  edit / view
     *    c                文件类型 1:正式文件  2：历史文件  3：附件
     *    isWatermark      是否开启水印 0 / 1
     *    isUserAcl        是否自定义用户权限 0 / 1
     * @return
     */
    public Map<String, Object> getFileInfo(String fileId, TempUser user, Map<String, Object> param) throws Exception {

        OnFile file = onFileService.getById(fileId);
        //处理 文件信息
        Map<String, Object> fileMap = JSONUtil.toBean(JSONUtil.toJsonStr(file),Map.class);
        fileMap.put("fileId",file.getFileId());
        fileMap.put("fileName",file.getFileName());
        fileMap.put("fileType",file.getFileType());
        // 用户信息


        // 用户权限
        UserAcl userAcl = null;
        if (param.containsKey("isUserAcl") && Integer.valueOf(param.get("isUserAcl").toString()) == 1){
            // 开启用户权限  todo
            userAcl = new UserAcl();
            userAcl.setComment(ConstantsWPS.USER_COMMENT_Y);
        }
        // 水印信息
        FileWatermark fileWatermark = null;
        if (param.containsKey("isWatermark") && Integer.valueOf(param.get("isWatermark").toString()) == 1){
            // 开启水印 设置水印内容 todo
            fileWatermark = new FileWatermark();
            fileWatermark.setType(1);
            fileWatermark.setValue("水印内容");
        }

        //生成配置信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(file.getFileId());
        fileInfo.setName(file.getFileName());
        fileInfo.setVersion(separateVer(file.getVersion()));
        fileInfo.setSize(file.getFileSize());
        //创建者
        fileInfo.setCreator(file.getUserId());

        //创建时间
        long createm_time = stringToDate(file.getCreatedTime(),"yyyy-MM-dd").getTime() / 1000;
        String createmtime = String.valueOf(createm_time).substring(0, 10);
        fileInfo.setCreate_time(Long.valueOf(createmtime));

        fileInfo.setModifier(user.getUserId());

        //更新时间
        long modify_time = getCurrentDateTime().getTime() / 1000;
        String modifytime = String.valueOf(modify_time).substring(0, 10);
        fileInfo.setModify_time(Long.valueOf(modifytime));

        UserPermission permission = UserPermission.read;
        if ("edit".equals(param.get("mode"))){
            permission = UserPermission.write;
        }
        UserInfo userInfo = new UserInfo(user.getUserId(),user.getUserName(), permission);

        Map<String, Object> config = wpsOfficeAPI.generateConfig(fileMap,fileInfo,userAcl,fileWatermark,userInfo);

        return config;
    }

    /**
     * 字符串转换为日期java.util.Date
     */
    private Date stringToDate(String dateText, String format) {
        if (dateText == null) {
            return null;
        }
        dateText = dateText.replaceAll("\\+", " ");
        DateFormat df = null;
        try {
            if (format == null) {
                df = new SimpleDateFormat();
            } else {
                df = new SimpleDateFormat(format);
            }
            // setLenient avoids allowing dates like 9/32/2001
            // which would otherwise parse to 10/2/2001
            df.setLenient(false);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return df.parse(dateText);
        } catch (ParseException e) {
           e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回当前时间
     * @return 返回当前时间
     */
    private Date getCurrentDateTime() {
        java.util.Calendar calNow = java.util.Calendar.getInstance();
        java.util.Date dtNow = calNow.getTime();
        return dtNow;
    }

    /**
     * 获取用户信息
     * @param map
     * @return
     */

    public Map<String, Object> getUserInfo(Map map) throws SQLException {

        Map<String,Object> users = new HashMap<>();
        List<String> ids = (List<String>) map.get("ids");

        // 查询用户信息 todo
        List<Map<String, Object>> list =  new ArrayList<>();

        users.put("users",list);
        return users;
    }

    /**
     * 文件在线人数 todo
     * @param
     * @return
     */

    public Map<String, Object> getFileOnline(Map<String, Object> param,Map<String, Object> map) {
        log.info("自定义参数：{}===wps参数：{}",param,map);
        return null;
    }

    /**
     * 文件保存
     * @param fileId
     * @param file   文件内容
     * @return
     */
    public Map<String, Object> fileSave(String fileId,byte[] file,TempUser user,Map<String, Object> param) {
        //处理 文件信息
        OnFile onFile= onFileService.getById(fileId);
        try {
            fileId =  onFileService.saveFile(file,onFile.getFileType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String version = onFile.getVersion();

        String[] split = version.split("\\.");
        version = split[0]+"."+split[1]+"."+ (Integer.valueOf(split[2])+1);

        OnFile newOnFile = new OnFile();
        newOnFile.setFileId(fileId);
        newOnFile.setFileName(onFile.getFileName());
        newOnFile.setVersion(version);
        newOnFile.setFileSize((long) file.length);
        newOnFile.setFileType(onFile.getFileType());
        onFileService.save(newOnFile);


        //生成返回信息
        FileInfo info = new FileInfo();
        info.setId(newOnFile.getFileId());
        info.setName(newOnFile.getFileName());
        info.setVersion(Integer.valueOf(version.replaceAll("\\.","")));
        info.setSize((long) file.length);
        info.setDownload_url(wpsOfficeAPI.getDownlaodUrl(fileId));

        Map<String,Object> fileParams = new HashMap<>();
        fileParams.put("file", info);
        return fileParams;
    }


    /**
     * 获取指定版本信息
     * @param
     * @return
     */
    public Map<String, Object> getFileVer(String fileId,Integer version,TempUser user,Map<String, Object> param)throws Exception {
        //处理 文件信息
        OnFile onFile = onFileService.getById(fileId);

        String ver = separateNumber(version,onFile.getVersion());

        onFile = onFileService.getOne(new LambdaQueryWrapper<OnFile>().eq(OnFile::getFileId,fileId).eq(OnFile::getVersion,ver));

        //创建时间
        long createm_time = stringToDate(onFile.getCreatedTime(),"yyyy-MM-dd").getTime() / 1000;
        String createmtime = String.valueOf(createm_time).substring(0, 10);
        //更新时间
        long modify_time = getCurrentDateTime().getTime() / 1000;
        String modifytime = String.valueOf(modify_time).substring(0, 10);

        String creator = onFile.getUserId();

        // 组合返回数据
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> resultFileInfo = new HashMap<>();
        resultFileInfo.put("id",fileId);
        resultFileInfo.put("name",onFile.getFileName());
        resultFileInfo.put("version",version);
        resultFileInfo.put("size",onFile.getFileSize());
        resultFileInfo.put("create_time",createmtime);
        resultFileInfo.put("creator",creator);
        resultFileInfo.put("modify_time", modifytime);
        resultFileInfo.put("modifier",user.getUserId());
        resultFileInfo.put("download_url",wpsOfficeAPI.getDownlaodUrl(onFile.getFileId()));

        result.put("file",resultFileInfo);

        return result;
    }

    /**
     * 把版本号转数字
     * 0.0.0 ==> 0    0.0.1 ==> 1
     * 1.0.0 ==> 100  0.1.2 ==> 12
     */
    private Integer separateVer(String version){
        return Integer.valueOf(version.replaceAll("\\.",""));
    }
    /**
     * 把数字转为版本号
     * @param number
     * @param version 最新的版本号
     * @return
     */
    private String separateNumber(int number,String version) {
        String[] split = version.split("\\.");

        // 如果小于指定数字
        if (number < 10 || number <= Integer.valueOf(split[2])){
            return "0.0."+number;
        }
        StringBuilder result = new StringBuilder();
        String numberStr = String.valueOf(number);

        for (int i = 0 ;i<split.length;i++) {
            if (Integer.valueOf(split[i]) == 0) {
                result.append("0");
                if (i != 2){
                    result.append(".");
                }
                continue;
            }
            int len = split[i].length();
            String v = numberStr.substring(0, len);

            if (Integer.valueOf(v) <= Integer.valueOf(split[i])){
                result.append(v);
                if (i != 2){
                    result.append(".");
                }
            }
            numberStr = numberStr.substring(len);
        }
        return result.toString();
    }

    /**
     * 重命名
     * @param
     * @return
     */

    public void fileRename(String fileId, String name,Map<String, Object> param) throws Exception {
        OnFile file = new OnFile();
        file.setFileId(fileId);
        file.setFileName(name);
        onFileService.updateById(file);
    }

    /**
     * 获取所有历史版本信息
     * @param user 用户信息
     * @param map  {id，offset，count}
     * @param param 自定义参数
     * @return
     */
    public Map<String, Object> fileHistory(String fileId,String token,TempUser user,Map map,Map<String, Object> param) throws Exception {
        String id = map.containsKey("id") ? map.get("id").toString() : "";
        //处理 文件信息
        OnFile onFile = onFileService.getById(fileId);

        //创建者
        Map<String, Object> creator = new HashMap<>();
        creator.put("id",onFile.getFileId());
        creator.put("name",onFile.getUserName());
        creator.put("avatar_url",wpsOfficeAPI.getConfiguration().getLocalhostAddress()+ avatarUrl +"头像接口");
        // 修改者
        Map<String, Object> modifier = new HashMap<>();
        modifier.put("id",user.getUserId());
        modifier.put("name",user.getUserName());
        modifier.put("avatar_url",wpsOfficeAPI.getConfiguration().getLocalhostAddress()+ avatarUrl +"头像接口");


        /**
         * todo 获取历史版本
         */
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> historieFile = new ArrayList<>();

        //创建时间
        long createm_time = stringToDate(onFile.getCreatedTime(),"yyyy-MM-dd").getTime() / 1000;
        String createmtime = String.valueOf(createm_time).substring(0, 10);
        //更新时间
        long modify_time = getCurrentDateTime().getTime() / 1000;
        String modifytime = String.valueOf(modify_time).substring(0, 10);

        // 组合数据
        for (Map<String, Object> file : listMap) {
            Map<String, Object> resultFileInfo = new HashMap<>();
            resultFileInfo.put("id",fileId);
            resultFileInfo.put("name",file.get("file_name"));
            resultFileInfo.put("version",file.get("file_name"));
            resultFileInfo.put("size",file.get("file_size"));
            resultFileInfo.put("create_time", createmtime);
            resultFileInfo.put("modify_time", modifytime);
            resultFileInfo.put("creator",creator);
            resultFileInfo.put("modifier",modifier);

            historieFile.add(resultFileInfo);
        }


        // 组合返回数据
        Map<String, Object> histories = new HashMap<>();
        histories.put("histories",historieFile);
        return histories;
    }

    /**
     * 回调通知
     * @param param
     * @param onnotifyMessage
     * @return
     */
    public void onnotify(Map<String, Object> param, OnnotifyMessage onnotifyMessage) {
        log.info("自定义参数：{}===wps参数：{}",param,onnotifyMessage);
        wpsOfficeAPI.handleOnnotifyMessage(onnotifyMessage);
    }

    /**
     * 文件复制粘贴
     * @param map
     * @return
     */

    public Map<String, Object> fileCopy( Map map) {
        return null;
    }

}
