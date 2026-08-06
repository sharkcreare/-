package com.pzx.knowledge.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;                // 用户ID
    private String username;        // 用户名
    private String nickname;        // 昵称
    private String avatar;          // 头像URL
    private String email;           // 邮箱
}