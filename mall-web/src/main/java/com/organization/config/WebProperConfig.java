package com.organization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther:zhw
 * @Description
 * @Date:Created in 11:26 2019/6/24
 **/
@Configuration
@PropertySource("classpath:common.properties")
public class WebProperConfig {

    @Value("${login.location}")
    private String loginLocation;

    @Value("${cooikeDomain}")
    private String cooikeDomain;

    public String getLoginLocation() {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public String getCooikeDomain() {
        return cooikeDomain;
    }

    public void setCooikeDomain(String cooikeDomain) {
        this.cooikeDomain = cooikeDomain;
    }
}
