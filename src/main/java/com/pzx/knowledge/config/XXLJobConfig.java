package com.pzx.knowledge.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class XXLJobConfig {
    @Value("${xxl.job.admin.addresses:http://localhost:8088/xxl-job-admin}")
    private String adminAddress;


    @Value("${xxl.job.accessToken:}")
    private String accessToken;


    @Value("${xxl.job.executor.appname:knowledge-system}")
    private String appName;



    @Value("${xxl.job.executor.port:9999}")
    private int port;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info("XX-JOB 执行器初始化：adminAddresses:{},appName:{},port:{}", adminAddress, appName, port);

        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(adminAddress);
        executor.setAppname(appName);
        executor.setPort(port);
        executor.setAccessToken(accessToken);
        return executor;

    }

}
