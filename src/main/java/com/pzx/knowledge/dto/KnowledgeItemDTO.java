package com.pzx.knowledge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class KnowledgeItemDTO {
    @NotBlank(message = "标题不能为空")
    private String title;           // 标题

    @NotBlank(message = "内容不能为空")
    private String content;         // 正文内容

    @NotBlank(message = "类型不能为空")
    private String contentType;     // 内容类型：NOTE/WEB/CODE/PDF

    private String summary;         // 摘要/简介
    private String sourceUrl;       // 来源链接
    private Boolean isTop;          // 是否置顶
    private List<Long> tagIds;      // 关联标签ID列表
}