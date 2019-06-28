package com.organization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @Auther:zhw
 * @Description
 * @Date:Created in 15:27 2019/6/11
 **/
@Configuration
public class MutipartResolverConfig {
    // 显示声明CommonsMultipartResolver为mutipartResolver  
//    @Bean(name="multipartResolver")
//    public MultipartResolver multipartResolver(){
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setDefaultEncoding("UTF-8");
//        // resolver.setResolveLazily(true);// resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常  
//        // resolver.setMaxInMemorySize(40960);  
//        resolver.setMaxUploadSize(10*1024*1024);// 上传文件大小 5M 5*1024*1024  
//        return resolver;
//    }
}
