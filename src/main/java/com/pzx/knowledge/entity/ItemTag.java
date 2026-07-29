package com.pzx.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("item_tag")
public class ItemTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long itemId;
    private Long tagId;
}