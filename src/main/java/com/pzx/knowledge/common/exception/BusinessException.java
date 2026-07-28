package com.pzx.knowledge.common.exception;

import com.pzx.knowledge.common.result.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    // 持有错误码枚举（code + message）
    private final ResultCode resultCode;

    // 构造器1：直接传错误枚举，提示文字用枚举自带的message
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode =resultCode;
    }

    // 构造器2：传错误枚举 + 自定义提示消息，覆盖默认message
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }
}