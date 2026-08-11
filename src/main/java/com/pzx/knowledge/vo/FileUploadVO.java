package com.pzx.knowledge.vo;

import lombok.Data;

/** 文件上传结果 VO */
@Data
public class FileUploadVO {
    private String objectName;   // MinIO 对象名，形如 u1/xxxx.jpg
    private String url;          // 预签名访问地址
}