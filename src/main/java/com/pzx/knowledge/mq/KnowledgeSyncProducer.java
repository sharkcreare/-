package com.pzx.knowledge.mq;

import com.pzx.knowledge.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KnowledgeSyncProducer {
    private final  RabbitTemplate rabbitTemplate;
    public void sendSyncMessage(KnowledgeSyncMessage message) {
        rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE_KNOWLEDGE,
                        RabbitMQConfig.ROUTING_SYNC,
                        message
                );

            log.info("发送同步信息：itemId={},action={}",message.getItemId(),message.getAction());
    }
}
