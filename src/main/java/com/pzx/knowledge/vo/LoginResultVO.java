package com.pzx.knowledge.vo;

import lombok.Data;

@Data
public class LoginResultVO {
    private String token;           // JWT访问令牌
    private UserVO user;            // 当前用户信息
}