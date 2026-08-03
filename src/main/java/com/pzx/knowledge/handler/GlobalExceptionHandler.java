package com.pzx.knowledge.handler;

import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.Result;
import com.pzx.knowledge.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

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
//@Valid 参数校验失败
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result <?> handleValidation(MethodArgumentNotValidException e) {
        String msg =e.getBindingResult().getFieldErrors().stream()
                .map(err->err.getField()+":"+err.getDefaultMessage())
                .collect(Collectors.joining(";"));
        log.warn("参数校验失败：{}", msg);
    return Result.fail(ResultCode.BAD_REQUEST,msg);
    }

//    token 无效 / 没登录
    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handAuthException(AuthenticationException e){
        log.warn("用户认证失败：{}", e.getMessage());
        return Result.fail(ResultCode.UNAUTHORIZED);
        }
//    登录成功但是没权限
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDenied(AccessDeniedException e){
        log.warn("权限不足：{}", e.getMessage());
        return Result.fail(ResultCode.FORBIDDEN);
    }
}