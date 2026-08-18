package com.pzx.knowledge.config;


import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class SentinelConfig {


    @PostConstruct
    public void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();


        FlowRule createRule = new FlowRule("knowledge_create");
        createRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        createRule.setCount(10);
        rules.add(createRule);

        FlowRule loginRule = new FlowRule("auth_login");
        loginRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        loginRule.setCount(5);
        rules.add(loginRule);


        FlowRule uploadRule = new FlowRule("file_upload");
        uploadRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        uploadRule.setCount(5);
        rules.add(uploadRule);


        FlowRuleManager.loadRules(rules);
        log.info("Sentinel 限流规则初始化完成，共{}条规则",rules.size());
    }

}
