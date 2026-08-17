package com.pzx.knowledge.mq;

import com.pzx.knowledge.config.RabbitMQConfig;
import com.pzx.knowledge.document.KnowledgeItemDocument;
import com.pzx.knowledge.entity.KnowledgeItem;
import com.pzx.knowledge.service.KnowledgeItemService;

import com.pzx.knowledge.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KnowledgeSyncConsumer {
    private final SearchService searchService;
    private final KnowledgeItemService knowledgeItemService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SYNC_ES)
    public void handleMessage(KnowledgeSyncMessage message){
        if(message ==null||message.getItemId()==null){
            log.warn("收到空消息,忽略");
            return;
        }else {
            log.info("收到同步消息：itemId={},action={}",message.getItemId(),message.getAction());
        }
        try {
            if ("delete".equals(message.getAction())) {
                searchService.deleteFromEs(message.getItemId());
            }else{
                KnowledgeItem item = knowledgeItemService.getById(message.getItemId());
                if (item == null) {
                    searchService.deleteFromEs(message.getItemId());
                    return;
                }
                KnowledgeItemDocument doc=new KnowledgeItemDocument();
                doc.setId(item.getId());
                doc.setUserId(item.getUserId());
                doc.setTitle(item.getTitle());
                doc.setContent(item.getContent());
                doc.setSummary(item.getSummary());
                doc.setContentType(item.getContentType());
                doc.setSourceUrl(item.getSourceUrl());
                doc.setIsFavorite(item.getIsFavorite());
                doc.setIsTop(item.getIsTop());
                doc.setViewCount(item.getViewCount());
                doc.setCreatedAt(item.getCreatedAt());
                doc.setUpdatedAt(item.getUpdatedAt());
                searchService.syncToEs(doc);
            }
        } catch (Exception e) {
            log.error("同步ES失败：itemId={}",message.getItemId(),e);
        }
    }
}
