package com.example.wps_demo.controller;

import com.example.wps_demo.service.TempUser;
import com.example.wps_demo.service.WPSCallBackService;
import com.office.office.wps.dto.OnnotifyMessage;
import com.office.tools.OfficeResult;
import com.office.tools.wps.RequestParameter2MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 项目名： spring-boot-onlyOffice
 * 包路径： com.example.wps_demo.controller
 * 作者：   TongHui
 * 创建时间: 2024-03-27 18:26
 * 描述: wps中台回调地址
 * 版本: 1.0
 */
@RestController
@RequestMapping("/v1/3rd")
public class WPSCallBackController {

    @Resource
    private WPSCallBackService wpsCallBackService;

    /**
     * 获取文件元数据
     * @return
     */
    @GetMapping("/file/info")
    public Object getFileInfo(HttpServletRequest request) {
        try {
            //获取自定义参数
            Map<String, Object> param = RequestParameter2MapUtil.requestParam2Map(request);
            String fileId = request.getHeader("X-Weboffice-File-Id");
            String token = request.getHeader("X-Wps-Weboffice-Token");

            //校验token todo
            TempUser user = TempUser.getUser();

            return wpsCallBackService.getFileInfo(fileId, user,param);
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

    /**
     * 获取用户信息
     * @param map
     * @return
     */
    @PostMapping("/user/info")
    public Object getUserInfo(@RequestBody Map map, HttpServletRequest request) {
        try {
            //获取自定义参数
            Map<String, Object> param = RequestParameter2MapUtil.requestParam2Map(request);
            String fileId = request.getHeader("X-Weboffice-File-Id");
            String token = request.getHeader("X-Wps-Weboffice-Token");

            //校验token todo


            return wpsCallBackService.getUserInfo(map);
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

    /**
     * 文件状态
     * @param
     * @return
     */
    @PostMapping("/file/online")
    public Object getFileOnline(HttpServletRequest request,@RequestBody Map map) {
        try {
            //获取自定义参数
            Map<String, Object> param = RequestParameter2MapUtil.requestParam2Map(request);

            String fileId = request.getHeader("X-Weboffice-File-Id");
            String token = request.getHeader("X-Wps-Weboffice-Token");

            wpsCallBackService.getFileOnline(param,map);
            return new OfficeResult(200,"ok","","");
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

    /**
     * 文件保存
     * @param
     * @return
     */
    @PostMapping("/file/save")
    public Object fileSave(HttpServletRequest request) {
        try {
            //获取自定义参数
            Map<String, Object> param = RequestParameter2MapUtil.requestParam2Map(request);


            String fileId = request.getHeader("X-Weboffice-File-Id");
            String token = request.getHeader("X-Wps-Weboffice-Token");
            /**
             * auto ：自动保存机制触发的新版本：
             * 1. 所有用户1分钟内未修改会触发的保存
             * 2.版本回退触发的保存
             * 3. WebSocket链接断开触发的保存
             * manual ：用户手动保存触发的新版本：
             * 1. 用户手动关闭网页触发的保存
             * 2. 用户通Ctrl+S触发的保存
             */
            String saveType = request.getHeader("X-Weboffice-Save-Type");

            //校验token todo
            TempUser user = TempUser.getUser();

            //转换request，解析出request中的文件
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> fileList = multipartRequest.getFiles("file");
            MultipartFile file = fileList.get(0);
            byte[] bytes = file.getBytes();



            return wpsCallBackService.fileSave(fileId,bytes,user,param);
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

    /**
     * 获取指定版本信息
     * @param version
     * @return
     */
    @GetMapping("/file/version/{version}")
    public Object getFileVer(HttpServletRequest request,@PathVariable Integer version) {
        try {
            //获取自定义参数
            Map<String, Object> param = RequestParameter2MapUtil.requestParam2Map(request);

            String fileId = request.getHeader("X-Weboffice-File-Id");
            String token = request.getHeader("X-Wps-Weboffice-Token");
            //校验token TODO
            TempUser user = TempUser.getUser();

            return wpsCallBackService.getFileVer( fileId, version, user, param);
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

    /**
     * 重命名
     * @param map
     * @return
     */
    @PutMapping("/file/rename")
    public OfficeResult fileRename(HttpServletRequest request,@RequestBody Map map) {
        try {
            //获取自定义参数
            Map<String, Object> param = RequestParameter2MapUtil.requestParam2Map(request);

            String fileId = request.getHeader("X-Weboffice-File-Id");
            String token = request.getHeader("X-Wps-Weboffice-Token");
            //校验token
            //校验token TODO


            wpsCallBackService.fileRename(fileId,map.get("name").toString(),param);
            return new OfficeResult(200,"ok","","");
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

    /**
     * 获取所有版本信息
     * @param map
     * @return
     */
    @PostMapping("/file/history")
    public Object fileHistory(HttpServletRequest request,@RequestBody Map map) {
        try {
            //获取自定义参数
            Map<String, Object> param = RequestParameter2MapUtil.requestParam2Map(request);

            String fileId = request.getHeader("X-Weboffice-File-Id");
            String token = request.getHeader("X-Wps-Weboffice-Token");
            //校验token TODO
            TempUser user = TempUser.getUser();


            return wpsCallBackService.fileHistory( fileId,token,user, map,param);
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

    /**
     * 回调通知
     * @param onnotifyMessage
     * cmd 命令参数，可选值：
     * OnlineFileCountCmd ：文档打开文件总数的回调命令
     * OperateRecordExport ：打印或导出操作命令
     * OpenPageCmd ：当用户调用WebSocket的session 接口获取token的时候会调用，即 CreateSession 接口调用
     * UserJoin ：当用户进行 WebSocket 连接建立之后，发送 session.open 的时候
     * UserQuit ：在关闭在线编辑协作页面的时候会调用通知用户退出
     * CommentAdd ：>=v6.1.2301.20230116在新增评论的时候会调用通知用户
     * @return
     */
    @PostMapping("/onnotify")
    public OfficeResult onnotify(HttpServletRequest request, @RequestBody OnnotifyMessage onnotifyMessage) {
        try {
            //获取自定义参数
            Map<String, Object> param = RequestParameter2MapUtil.requestParam2Map(request);

            String fileId = request.getHeader("X-Weboffice-File-Id");
            String token = request.getHeader("X-Wps-Weboffice-Token");

            wpsCallBackService.onnotify(param,onnotifyMessage);
            return new OfficeResult(200,"ok","","");
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

    /**
     * 文件复制粘贴
     * @param map
     * @return
     */
    @RequestMapping("/copy_control/paste")
    public OfficeResult fileCopy(HttpServletRequest request,@RequestBody Map map) {
        try {
            return new OfficeResult(200,"ok","","");
        }catch (Exception e) {
            e.printStackTrace();
            return new OfficeResult(40007,"CustomMsg","",e.getMessage());
        }
    }

}
