package com.pzx.knowledge.job;

import com.pzx.knowledge.mapper.KnowledgeItemMapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KnowledgeReportJob
{
    private final KnowledgeItemMapper knowledgeItemMapper;

    @XxlJob("knowledgeReportHandler")
    public void execute(){
        log.info("===== 开始执行知识库统计报表 =====");
        Long total = knowledgeItemMapper.selectCount(null);
        log.info("知识条目总数: {}", total);
    }
}
