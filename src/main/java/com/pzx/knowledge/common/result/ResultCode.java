package com.pzx.knowledge.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(0,"success"),
    BAD_REQUEST(400,"请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // 业务错误码
    USERNAME_EXISTS(1001, "用户名已存在"),
    USER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    ITEM_NOT_FOUND(2001, "知识条目不存在"),
    TAG_EXISTS(3001, "标签已存在"),
    TAG_NOT_FOUND(3002, "标签不存在");

    private final int code;
    private final String message;
}
