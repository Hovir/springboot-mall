package com.organization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther:zhw
 * @Description 静态变量 读取properties配置文件属性
 * @Date:Created in 11:26 2019/6/24
 **/
@Configuration
@PropertySource("classpath:common.properties")
public class CommConfig {

    public static String endpoint;
    public static String accessKeyId;
    public static String accessKeySecret;
    public static String bucketName;

    @Value("${endpoint}")
    public void setEndpoint(String endpoint) {
        CommConfig.endpoint = endpoint;
    }

    @Value("${accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        CommConfig.accessKeyId = accessKeyId;
    }

    @Value("${accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        CommConfig.accessKeySecret = accessKeySecret;
    }

    @Value("${bucketName}")
    public void setBucketName(String bucketName) {
        CommConfig.bucketName = bucketName;
    }
}
