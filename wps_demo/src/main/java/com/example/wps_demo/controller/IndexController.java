package com.example.wps_demo.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wps_demo.entity.OnFile;
import com.example.wps_demo.entity.Result;
import com.example.wps_demo.service.OnFileService;
import com.example.wps_demo.service.TempUser;
import com.office.config.oo.edit.FileUser;
import com.office.office.oo.OnlyOfficeAPI;
import com.office.office.wps.ConstantsWPS;
import com.office.office.wps.WPSOfficeAPI;
import com.office.tools.FileUtil;
import com.office.tools.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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
    private WPSOfficeAPI wpsOfficeAPI;


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
    @RequestMapping("/wps/{mode}/{id}")
    public ModelAndView openDocument(@PathVariable String mode,@PathVariable String id, Model model) {//@RequestParam("url") String url,
        OnFile onFile = onFileService.getById(id);

        /**
         * 自定义参数
         */
        Map<String,String> params = new HashMap<String,String>();

        String link = "";
        if ("edit".equals(mode)) {
            /**
             *  文件id                                  必填
             *  文件名称                                 必填
             *  是否传递token                            必填
             *  自定义参数                               非必填
             */
            link = wpsOfficeAPI.openDocument(id, onFile.getFileName(), ConstantsWPS.W_TOKENTYPE_YES, params);

        }
        if ("view".equals(mode)) {
            /**
             *  文件id                                  必填
             *  文件名称                                 必填
             *  预览类型                                 非必填
             *  是否传递token                            必填
             *  高清预览支持控制修订痕迹、评论是否显示等参数。 非必填
             *  自定义参数                               非必填
             */
            link = wpsOfficeAPI.openDocument(id,onFile.getFileName(),null,ConstantsWPS.W_TOKENTYPE_YES,null,params);
        }
        model.addAttribute("link",link);
        return new ModelAndView("wps");
    }



    @RequestMapping("/converted")
    public void converted(String id, String suffix,HttpServletResponse response) {
        try {
            OnFile onFile = onFileService.getById(id);
            String title = onFile.getFileName().replace(onFile.getFileType(),suffix);

            byte[] bytes = wpsOfficeAPI.convert(id, onFile.getFileName(), null, suffix);

            FileUtil.downloadAttachment(bytes,title,response);

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
