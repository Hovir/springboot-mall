package com.organization.intercetor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther:zhw
 * @Description
 **/
@Configuration
public class SessionInterceptor implements WebMvcConfigurer {


    @Bean
    public UserIntercetor userIntercetor(){
        return new UserIntercetor();
    }

    /**
     * 自定义拦截器，添加拦截路径和排除拦截路径
     * addPathPatterns():添加需要拦截的路径
     * excludePathPatterns():添加不需要拦截的路径
     */
    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> list = new ArrayList<String>();
        list.add("/");
        list.add("/userInfo/userLogin");
        list.add("/userInfo/userRegister");
        list.add("/dynamic/getDynamicListOnIndex");
        list.add("/userInfo/signOut");
        list.add("/**/error");
        list.add("/index.html");
        list.add("/static/**");
        registry.addInterceptor(userIntercetor()).addPathPatterns("/**").excludePathPatterns(list);
    }

    // 设置过滤器 允许跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }

    // 设置默认跳转页面
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        WebMvcConfigurer.super.addViewControllers(registry);
    }
}
