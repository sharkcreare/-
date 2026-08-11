package com.pzx.knowledge.service;

import com.pzx.knowledge.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

/** 文件存储服务接口 */
public interface FileService {

    FileUploadVO upload(MultipartFile file);

    InputStream download(String objectName);

    void delete(String objectName);

    FileUploadVO getPresignedUrl(String objectName);
}