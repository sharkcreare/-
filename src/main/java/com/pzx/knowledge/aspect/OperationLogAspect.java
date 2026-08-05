package com.pzx.knowledge.aspect;

import com.pzx.knowledge.annotation.OperationLog;
import com.pzx.knowledge.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Around("@annotation(opLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog opLog) throws Throwable {
        Long userId = UserContext.getUser();
        String module = opLog.module();
        String type = opLog.type();
        String desc = opLog.desc();

        log.info("[操作日志] 用户:{} | 模块:{} | 操作:{} | 描述:{} | 时间:{}",
                userId, module, type, desc, LocalDateTime.now());

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - start;

        log.info("[操作日志] 执行耗时: {}ms", elapsed);
        return result;
    }
}