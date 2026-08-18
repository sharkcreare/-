package com.pzx.knowledge.job;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheRefreshJob {
    private final CacheManager caffeineCacheManager;

    public CacheRefreshJob(@Qualifier ("caffeineCacheManager")CacheManager caffeineCacheManager) {
        this.caffeineCacheManager = caffeineCacheManager;

    }
    @XxlJob("cacheRefreshHandler")
    public void execute() {
        log.info("=====开始执行缓存刷新任务=====");
        try {
            caffeineCacheManager.getCacheNames().forEach(name -> {
                var cache = caffeineCacheManager.getCache(name);
                if (cache != null) {
                    cache.clear();
                    log.info("缓存已清空：{}", name);
                }
            });
            log.info("缓存刷新任务执行完毕");
        }catch (Exception e){
            log.error("缓存刷新任务失败",e);
            throw  e;
        }
    }
}
