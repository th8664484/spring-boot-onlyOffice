package com.example.onlyoffice_demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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

