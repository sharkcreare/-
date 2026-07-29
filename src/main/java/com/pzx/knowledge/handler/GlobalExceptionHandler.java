package com.pzx.knowledge.handler;

import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.Result;
import com.pzx.knowledge.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleException(BusinessException e) {
        return Result.fail(e.getResultCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("system error", e);
        return Result.fail(ResultCode.INTERNAL_ERROR);
    }
}