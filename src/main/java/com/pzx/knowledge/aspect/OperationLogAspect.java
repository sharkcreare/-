package com.pzx.knowledge.aspect;

import com.pzx.knowledge.annotation.OperationLog;
import com.pzx.knowledge.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 操作日志切面 —— 统一拦截 @OperationLog 标注的方法
 *
 * 设计要点：
 * 1. 用 @Around 包住方法调用，能同时拿到【成功/失败】和【耗时】
 * 2. 失败时也记录日志并原样抛出异常，绝不吞异常
 * 3. 生产环境建议把日志异步写入数据库表（op_log），这里留了 TODO
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Around("@annotation(opLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog opLog) throws Throwable {
        Long userId = UserContext.getUser();           // 从 ThreadLocal 取当前登录用户
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();       // 执行目标方法
            long cost = System.currentTimeMillis() - start;
            log.info("[操作日志-成功] 用户:{} | 模块:{} | 类型:{} | 描述:{} | 耗时:{}ms | 时间:{}",
                    userId, opLog.module(), opLog.type(), opLog.desc(), cost, LocalDateTime.now());
            return result;
        } catch (Throwable t) {
            long cost = System.currentTimeMillis() - start;
            log.error("[操作日志-失败] 用户:{} | 模块:{} | 类型:{} | 描述:{} | 耗时:{}ms | 异常:{}",
                    userId, opLog.module(), opLog.type(), opLog.desc(), cost, t.getMessage(), t);
            throw t;                                   // 关键：必须重新抛出，让业务异常继续走全局处理器
        }
        // TODO: 生产环境在此异步落库 op_log（可用 @Async 或 MQ）
    }
}