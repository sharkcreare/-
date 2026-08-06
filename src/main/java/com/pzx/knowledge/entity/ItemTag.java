package com.pzx.knowledge.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("knowledge_item_tag")
public class ItemTag {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID

    private Long itemId;            // 笔记ID

    private Long tagId;             // 标签ID

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt; // 创建时间
}