package com.pzx.knowledge.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TagVO {
    private Long id;                // 标签ID
    private Long userId;            // 所属用户ID
    private String name;            // 标签名称
    private String color;           // 标签颜色（十六进制）
    private Long itemCount;         // 关联笔记数量
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}