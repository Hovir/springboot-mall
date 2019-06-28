package com.organization;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Auther:zhw
 * @Description
 * @Date:Created in 10:42 2019/5/9
 **/
@SpringBootApplication
@MapperScan("com.organization.dao")
@EnableDubbo
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(ProviderApplication.class);
        // 禁用devTools热部署
        //System.setProperty("spring.devtools.restart.enabled", "false");
        // 禁用命令行更改application.properties属性
        sa.setAddCommandLineProperties(false);
        sa.run(args);
    }
}
