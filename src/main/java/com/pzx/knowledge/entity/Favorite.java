package com.pzx.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("favorite")
public class Favorite {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID

    private Long userId;            // 用户ID

    private Long itemId;            // 笔记ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt; // 创建时间
}