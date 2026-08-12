package com.pzx.knowledge.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class KnowledgeSearchDTO {
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;        // 页码

    @Min(value = 1, message = "每页条数不能小于1")
    private Integer pageSize = 10;      // 每页条数

    private String contentType;         // 内容类型：NOTE/WEB/CODE/PDF
    private Long tagId;                 // 标签ID
    private String keyword;             // 搜索关键词
}