package com.pzx.knowledge.config;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_KNOWLEDGE = "knowledge.exchange";
    public static final String QUEUE_SYNC_ES = "knowledge.sync.es.queue";
    public static final String QUEUE_FILE_PROCESS = "knowledge.file.process.queue";
    public static final String ROUTING_SYNC = "knowledge.sync";


    @Bean
    public TopicExchange knowledgeExchange() {
        return new TopicExchange(EXCHANGE_KNOWLEDGE,true,false);
    }

    @Bean
    public Queue syncEsQueue() {
        return QueueBuilder.durable(QUEUE_SYNC_ES).build();
    }
    @Bean
    public Queue fileProcessQueue() {
        return QueueBuilder.durable(QUEUE_FILE_PROCESS).build();
    }

    @Bean
    public Binding syncEsBinding(){
        return BindingBuilder.bind(syncEsQueue()).to(knowledgeExchange()).with(ROUTING_SYNC);
    }

    @Bean
    public Binding fileProcessBinding(){
        return BindingBuilder.bind(fileProcessQueue()).to(knowledgeExchange()).with("knowledge.file.*");
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }


}
