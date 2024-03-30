package com.example.wps_demo.service;

import com.office.core.MessageHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
public class MyMessageHandle implements MessageHandle {

    @Override
    public void OnlineFile(Map<String, Object> body) {
        log.info("================{}=============",body);
    }

    @Override
    public void OperateRecordExport(Map<String, Object> body) {
        log.info("================{}=============",body);
    }

    @Override
    public void OpenPage(Map<String, Object> body) {
        log.info("================{}=============",body);
    }

    @Override
    public void UserQuit(Map<String, Object> body) {
        log.info("================{}=============",body);
    }

    @Override
    public void UserJoin(Map<String, Object> body) {
        log.info("================{}=============",body);
    }

    @Override
    public void FileDirty(Map<String, Object> body) {
        log.info("================{}=============",body);
    }

    @Override
    public void FileClear(Map<String, Object> body) {
        log.info("================{}=============",body);
    }

    @Override
    public void CommentAdd(Map<String, Object> body) {
        log.info("================{}=============",body);
    }
}
