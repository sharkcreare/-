package com.pzx.knowledge.lock;

import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


@Component
@RequiredArgsConstructor
@Slf4j
public class LockUtil {
    private final RedissonClient redissonClient;

    public <T> T executeWithLock (String lockKey, long waitTime,
                                  long leaseTime, TimeUnit unit, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);
        try {

            boolean locked =lock.tryLock(waitTime, leaseTime, unit);
            if (!locked) {
                log.warn("获取分布式锁失败:{}", lockKey);
                throw new BusinessException(ResultCode.SERVER_BUSY);
            }
            return supplier.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ResultCode.SERVER_BUSY);

        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }



    public void executeWithLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit, Runnable runnable) {
        executeWithLock(lockKey, waitTime, leaseTime, unit, ()->{
            runnable.run();
            return null;
        });
    }





}
