package com.oo.demo.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @BelongsProject: leaf-onlyoffice
 * @BelongsPackage: com.ideayp.leaf.entity
 * @Author: TongHui
 * @CreateTime: 2022-11-14 10:20
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@TableName("on_file")
public class OnFile {
    @TableId
    private String fileId;
    @TableField("`version`")
    private String version;
    private String createdTime;
    private String userName;
    private String userId;
    private String fileName;
    private String fileType;
    private Long fileSize;
}

