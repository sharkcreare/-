package com.pzx.knowledge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("knowledge_item_tag")
public class ItemTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long itemId;
    private Long tagId;
}