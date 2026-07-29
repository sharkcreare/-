package com.pzx.knowledge.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class KnowledgeItemVO {
    private Long userid;
    private Long id;
    private String title;
    private String content;
    private String contentType;
    private String summary;
    private String sourceUrl;
    private Boolean isFavorite;
    private Boolean isTop;
    private Integer viewCount;
    private List<String> tags;       // 标签名称列表
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}