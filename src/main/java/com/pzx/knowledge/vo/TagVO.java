package com.pzx.knowledge.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TagVO {
    private Long id;
    private Long userId;
    private String name;
    private String color;
    private Long itemCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}