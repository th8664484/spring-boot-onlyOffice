package com.oo.demo.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oo.demo.entity.OnFile;
import com.oo.demo.entity.Result;
import com.oo.demo.service.FileService;
import com.oo.demo.service.OnFileService;
import com.oo.demo.service.TempUser;
import com.oo.onlyoffice.api.OnlyServiceAPI;
import com.oo.onlyoffice.dto.edit.FileUser;
import com.oo.onlyoffice.tools.FileUtil;
import com.oo.onlyoffice.tools.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * @BelongsProject: leaf-onlyoffice
 * @BelongsPackage: com.ideayp.leaf.onlyoffice.controller
 * @Author: TongHui
 * @CreateTime: 2022-11-08 18:11
 * @Description: TODO
 * @Version: 1.0
 */
@Controller
@Slf4j
public class IndexController {



    @Autowired
    private OnFileService onFileService;
    @Autowired
    private FileService fileService;
    @Autowired
    private OnlyServiceAPI onlyServiceAPI;


    @RequestMapping("/")
    public String filesView(Model model){
        return "/index";
    }


    @RequestMapping("/file/all")
    @ResponseBody
    public Object file(@RequestParam Map<String,String> parameter){
        int page = Integer.parseInt(parameter.get("page"));
        int limit = Integer.parseInt(parameter.get("limit"));

        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.notLike("id","MD");
        queryWrapper.orderByDesc("created_time");

        Page<OnFile> p = new Page<>(page,limit);
        p = onFileService.page(p,queryWrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","SUCCESS");
        map.put("count", p.getTotal());
        map.put("data", p.getRecords());
        return map;
    }

    @PostMapping("/files/upload")
    @ResponseBody
    public Object upload(@RequestParam("file") MultipartFile[] file){
        int i = 0;
        for(MultipartFile multipartFile:file){
            CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartFile;

            String filename = multipartFile.getOriginalFilename();
            // 获取文件后缀
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);
            String fileId = "";
            try {
                fileId = onFileService.saveFile(commonsMultipartFile.getBytes(), suffix);
            } catch (Exception e) {
                e.printStackTrace();
            }



            OnFile onFile = new OnFile();
            onFile.setFileId(fileId);
            onFile.setFileName(filename);
            onFile.setVersion("1.0.0");
            onFile.setFileSize(commonsMultipartFile.getSize());
            onFile.setFileType(suffix);
            onFileService.save(onFile);

            i++;
        }
        if (file.length == i){
            return Result.OK("文件上传成功");
        }
        return Result.error("文件上传失败");
    }


    /**
     * 打开编辑
     */
    @RequestMapping("/onlyOffice/{mode}/{id}")
    public ModelAndView openDocument(@PathVariable String mode,@PathVariable String id, Model model) {//@RequestParam("url") String url,
        log.info("only office key:" + id);
        OnFile onFile = onFileService.getById(id);


        /**
         * 必要步骤
         */
        FileUser user = new FileUser();
        user.setId(TempUser.getUserId());
        user.setName(TempUser.getUserName());
        SecurityUtils.setUserSession(user);
        /**
         * 必要步骤
         */
        Map<String,Object> map = new HashMap<>();
        map.put("fileId",onFile.getFileId());
        map.put("fileName",onFile.getFileName());
        map.put("fileType",onFile.getFileType());
        map.put("fileSize",onFile.getFileSize());
        map.put("version",onFile.getVersion());

        Map config = onlyServiceAPI.openDocument(map, mode, false);

        SecurityUtils.removeUserSession();

        String s = JSON.toJSONString(config);
        log.info("only office config:" + s);
        model.addAllAttributes(config);
        return new ModelAndView("onlyOffice");
    }

    /**
     * 文档编辑服务使用JavaScript API通知callbackUrl，向文档存储服务通知文档编辑的状态。
     * 文档编辑服务使用具有正文中的信息的POST请求。
     * https://api.onlyoffice.com/editors/callback
     * <p>
     * 当我们关闭编辑窗口后，十秒钟左右only office会将它存储的我们的编辑后的文件
     * 0 - 找不到具有密钥标识符的文档
     * 1 - 正在编辑文档
     * 2 - 文档已准备好保存
     * 3 - 发生文档保存错误
     * 4 - 不作任何更改就关闭文档
     * 6 - 正在编辑文档，但保存当前文档状态
     * 7 - 强制保存文档时发生错误
     *
     */
    @RequestMapping("/onlyOffice/save")
    public void saveFile(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            // 获取传输的json数据
            Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
            String body = scanner.hasNext() ? scanner.next() : "";
            JSONObject jsonObject = JSONObject.parseObject(body);
            log.info("{}", jsonObject);

            fileService.documentSave(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("{\"error\":-1}");
            return;
        }
        /*
         * status = 1，我们给onlyOffice的服务返回{"error":"0"}的信息。
         * 这样onlyOffice会认为回调接口是没问题的，这样就可以在线编辑文档了，否则的话会弹出窗口说明
         */
        if (Objects.nonNull(writer)) {
            writer.write("{\"error\":0}");
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    public Result<?> save(String id){
        OnFile onFile = onFileService.getById(id);

        String key = onlyServiceAPI.getKey(onFile.getFileId());
        String msg = onlyServiceAPI.save(key,TempUser.getUserId());

        return Result.OK(msg);
    }


    @RequestMapping("/converted")
    public void converted(String id, String suffix,HttpServletResponse response) {
        try {
            OnFile onFile = onFileService.getById(id);
            String title = onFile.getFileName().replace(onFile.getFileType(),suffix);
            String path = onlyServiceAPI.converted(onFile.getFileType(),onFile.getFileId(),suffix,title,null);

            FileUtil.downloadAttachment(path,title,response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/del")
    @ResponseBody
    public Object delFile(String id){
        try {
            onFileService.removeFile(id);
            return Result.OK("删除成功","fileTable");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("删除失败");
    }

    /**
     *
     * @param id
     * @param isBrowser 用来判断是否是oo服务的回调下载  1.没有值 = oo服务回调下载。2.有值 = 页面手动下载
     * @param response
     */
    @RequestMapping("/download/{id}")
    public void download(@PathVariable String id,String isBrowser, HttpServletResponse response) {
        try {
            onFileService.download(id,isBrowser,response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
