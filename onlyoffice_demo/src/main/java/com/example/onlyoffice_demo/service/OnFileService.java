package com.example.onlyoffice_demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.onlyoffice_demo.entity.OnFile;

import javax.servlet.http.HttpServletResponse;


public interface OnFileService extends IService<OnFile> {

    String saveFile(byte[] bytes, String fileType) throws Exception;

    void removeFile(String id);

    void download(String id,String isBrowser, HttpServletResponse response);
}
