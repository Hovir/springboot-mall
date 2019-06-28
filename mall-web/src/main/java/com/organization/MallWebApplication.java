package com.organization;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//,MultipartAutoConfiguration.class
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})//因添加pagehelper依赖需要排除加载DataSourceAutoConfiguration类
//@ComponentScan(basePackages={"com.organization"})
@EnableDubbo
public class MallWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallWebApplication.class, args);
	}

}
