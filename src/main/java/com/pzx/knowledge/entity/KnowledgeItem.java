package com.pzx.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("knowledge_item")
public class KnowledgeItem {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID

    private Long userId;            // 所属用户ID

    private String title;           // 标题

    private String content;         // 正文内容

    private String contentType;     // 内容类型：NOTE/WEB/CODE/PDF

    private String summary;         // 摘要

    private String sourceUrl;       // 来源链接

    private Integer isFavorite;     // 是否收藏：1=是，0=否（冗余字段，便于查询）

    private Integer isTop;          // 是否置顶：1=是，0=否

    private Integer viewCount;      // 浏览次数

    @TableLogic
    private Integer deleted;        // 逻辑删除：1=已删除，0=正常

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间
}