package com.oo.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oo.onlyoffice.api.OnlyServiceAPI;
import com.oo.onlyoffice.dto.edit.FileUser;
import com.oo.onlyoffice.tools.SecurityUtils;
import com.oo.demo.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: onlyoffice-demo
 * @BelongsPackage: com.oo.demo.service
 * @Author: TongHui
 * @CreateTime: 2023-08-02 09:57
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Service
public class FileService {

    @Autowired
    private OnlyServiceAPI onlyServiceAPI;
    @Autowired
    private DemoService demoService;


    /**
     * 文档服务器 保存文件回调
     *
     * @param jsonObject
     */
    @Transactional(rollbackFor = Exception.class)
    public void documentSave(JSONObject jsonObject) {
        String key = "";
        try {
            int status = jsonObject.getIntValue("status");
            log.info("status[{}]", status);
            if (6 == status) {
                log.info("开始保存文件");

                JSONArray jsonArray = jsonObject.getJSONObject("history").getJSONArray("changes");
                JSONObject object = jsonArray.getJSONObject(0);
                String userId = object.getJSONObject("user").getString("id");
                FileUser fileUser = new FileUser();
                fileUser.setId(userId);
                SecurityUtils.setUserSession(fileUser);

                key = jsonObject.getString("key");

                //文件id
                String id = onlyServiceAPI.getFileId(key);


                //判断是否是最后一人进行保存
                int users = onlyServiceAPI.getUserNum(key);
                if (users > 1) {
                    return;
                }

                //历史版本最大个数 获取当前文件的历史版本数量
                Integer histNum = onlyServiceAPI.getHistNum();
                /**
                 * 如果有需要保存历史记录 可以进行相关操作
                 */
                if (null != histNum){

                }

                //处理文件的保存
                onlyServiceAPI.handlerStatus(jsonObject);


                log.info("保存文件结束");
                SecurityUtils.removeUserSession();
            } else if (0 == status || 2 == status || 4 == status) {
                onlyServiceAPI.close(jsonObject);
            } else if (3 == status || 7 == status) {
                //保存文档错误
                onlyServiceAPI.close(jsonObject);
            } else if (1 == status) {
                //文件服务的回调  获取key判断当前文档有多少人在这使用
                List<Map> actions = JSONObject.parseArray(jsonObject.getString("actions"), Map.class);
                if ((Integer) actions.get(0).get("type") == 1) {
                    log.info("当前用户有[{}]", jsonObject.getString("users"));
                    List<String> users = JSONObject.parseArray(jsonObject.getString("users"), String.class);
                    onlyServiceAPI.iskey(jsonObject.getString("key"), users.size());
                }
                if ((Integer) actions.get(0).get("type") == 0) {
                    onlyServiceAPI.iskey(jsonObject.getString("key"), null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }



    }


}
