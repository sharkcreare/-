package com.pzx.knowledge.controller;

import com.pzx.knowledge.annotation.OperationLog;
import com.pzx.knowledge.common.result.Result;

import com.pzx.knowledge.service.FileService;

import com.pzx.knowledge.vo.FileUploadVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
@Tag(name="文件管理")
public class FileController {
    private final FileService fileService;


    @OperationLog(module = "文件管理",type="上传",desc = "上传文件")
    @Operation(summary = "上传文件到minio")
    @PostMapping("/upload")
    public Result<FileUploadVO> upload(@RequestParam("file") MultipartFile file) {
        return Result.ok(fileService.upload(file));
    }
    @Operation(summary ="删除文件")
    @DeleteMapping
    @OperationLog(module = "文件管理",type="删除",desc = "删除文件")
    public Result<Void> delete(@RequestParam String objectName) {
        fileService.delete(objectName);
        return Result.ok();
    }
    @Operation(summary = "获取文件访问链接")
    @GetMapping("/presigned")
    public Result<FileUploadVO> getPresigned(@RequestParam String objectName) {

        return Result.ok(fileService.getPresignedUrl(objectName));
    }




}
