package com.oo.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oo.demo.entity.OnFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;


public interface OnFileService extends IService<OnFile> {

    String saveFile(byte[] bytes, String fileType) throws Exception;

    void removeFile(String id);

    void download(String id,String isBrowser, HttpServletResponse response);
}
