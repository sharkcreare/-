package com.pzx.knowledge.vo;

import lombok.Data;

@Data
public class LoginResultVO {
    private String token;
    private UserVO user;
}