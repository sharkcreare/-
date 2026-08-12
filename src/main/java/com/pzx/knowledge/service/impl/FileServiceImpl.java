package com.pzx.knowledge.service.impl;

import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.ResultCode;
import com.pzx.knowledge.config.MinioConfig;
import com.pzx.knowledge.service.FileService;
import com.pzx.knowledge.utils.UserContext;
import com.pzx.knowledge.vo.FileUploadVO;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;



    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Override
    public FileUploadVO upload(MultipartFile file) {
        Long userId = UserContext.getUser();
        if(userId == null){
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.FILE_UPLOAD_FAILED);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ResultCode.FILE_TOO_LARGE);
        }
        try{
            String bucket = minioConfig.getBucket();
            String originalName = file.getOriginalFilename();
            String suffix ="";
            if(originalName != null &&  originalName.contains(".")){
                suffix = originalName.substring(originalName.lastIndexOf("."));
            }

            String objectName = "u"+userId+"/"+UUID.randomUUID().toString().replace("-","")+suffix;
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(file.getInputStream(),file.getSize(),-1)
                            .contentType(file.getContentType())
                            .build());
            log.info("文件上传成功: {} -> {}", originalName, objectName);

            String url = getPresignedUrl(objectName).getUrl();
            FileUploadVO vo = new FileUploadVO();
            vo.setObjectName(objectName);
            vo.setUrl(url);
            return vo;
        }catch (Exception e){
            log.error("文件上传失败", e);
            throw new BusinessException(ResultCode.FILE_UPLOAD_FAILED);
        }

    }

    @Override
    public InputStream download(String objectName) {
        Long userId = UserContext.getUser();
        try{
            if (objectName == null || !objectName.startsWith("u" + userId + "/")) {
                throw new BusinessException(ResultCode.FILE_NOT_FOUND);
            }
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs
                            .builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .build());
            log.info("文件下载成功: {}", objectName);
            return inputStream;
        }catch (Exception e) {
            log.error("文件下载失败:{}", objectName, e);
            throw new BusinessException(ResultCode.FILE_NOT_FOUND);
        }
    }

    @Override
    public void delete(String objectName) {
        Long userId = UserContext.getUser();
        if(userId == null){
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
            if (objectName == null || !objectName.startsWith("u" + userId + "/")) {
                throw new BusinessException(ResultCode.FILE_NOT_FOUND);
            }

        try {
            minioClient.removeObject(RemoveObjectArgs
                    .builder()
                    .bucket(minioConfig.getBucket())
                    .object(objectName)
                    .build());
            log.info("文件删除成功：{}"  , objectName);
        }catch (Exception e) {
           log.error("文件删除失败：{}", objectName, e);
            throw new BusinessException(ResultCode.FILE_DELETE_FAILED);
        }

    }

    @Override
    public FileUploadVO getPresignedUrl(String objectName) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        if (objectName == null || !objectName.startsWith("u" + userId + "/")) {
            throw new BusinessException(ResultCode.FILE_NOT_FOUND);
        }

        try {
            String url =minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs
                            .builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(30, TimeUnit.MINUTES)
                            .build());
            FileUploadVO  vo = new FileUploadVO();
            vo.setObjectName(objectName);
            vo.setUrl(url);
            return vo;
        }catch (Exception e) {
            log.error("获取预签名URL失败：{}", objectName, e);
            throw new BusinessException(ResultCode.FILE_NOT_FOUND);
        }
    }
}
