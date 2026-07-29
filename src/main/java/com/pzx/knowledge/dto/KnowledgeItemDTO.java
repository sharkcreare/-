package com.pzx.knowledge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class KnowledgeItemDTO {
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    @NotBlank(message = "类型不能为空")
    private String contentType;   // NOTE / WEB / CODE / PDF

    private String summary;
    private String sourceUrl;
    private Boolean isTop;
    private List<Long> tagIds;    // 关联的标签ID列表
}