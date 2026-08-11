package com.pzx.knowledge.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class KnowledgeItemVO {
    private Long id;                // 笔记ID
    private String title;           // 标题
    private String content;         // 正文内容
    private String contentType;     // 内容类型：NOTE/WEB/CODE/PDF
    private String summary;         // 摘要
    private String sourceUrl;       // 来源链接
    private Boolean isFavorite;     // 是否已收藏
    private Boolean isTop;          // 是否置顶
    private Integer viewCount;      // 浏览次数
    private List<TagVO> tags;        // 标签名称列表
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}