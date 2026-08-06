package com.pzx.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID

    private String username;        // 用户名（唯一）

    private String password;        // 密码（BCrypt加密）

    private String nickname;        // 昵称

    private String avatar;          // 头像URL

    private String email;           // 邮箱

    private Integer status;         // 状态：1=正常，0=禁用

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间
}