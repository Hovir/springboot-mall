package com.organization.intercetor;

import com.organization.config.WebProperConfig;
import com.organization.entity.UserInfo;
import com.organization.utils.CookieUtil;
import com.organization.utils.FastJsonUtils;
import com.organization.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Auther:zhw
 * @Description
 **/
//@Component
public class UserIntercetor implements HandlerInterceptor{

    private static Logger logger = LoggerFactory.getLogger(UserIntercetor.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WebProperConfig webProperConfig;
    /*
     * 进入controller层之前拦截请求
     * 返回值：表示是否将当前的请求拦截下来  false：拦截请求，请求别终止。true：请求不被拦截，继续执行
     * Object obj:表示被拦的请求的目标对象（controller中方法）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, ServletException {
        System.out.println("请求地址："+request.getRequestURL());
//        System.out.println("执行到了preHandle方法，handler ：" + handler);

        // 获取请求中的cookie
        Cookie loginToken = CookieUtil.getCookie(request, "Login_token");
        // 获取cookie中的value
        String cookieValue = CookieUtil.getValue(loginToken);

        logger.info("进入拦截器，请求传入的cookie:"+loginToken+"请求传入的cookieValue:" + cookieValue);

        if (null == loginToken || cookieValue.equals("")){
            logger.info("请求cookie为空");
            // 跳转到登录
            response.sendRedirect(webProperConfig.getLoginLocation());
//            request.getRequestDispatcher("/").forward(request, response);
            return false;
        }

        // 获取cookie中的token
        Map<Object, Object> cookieValueMap = FastJsonUtils.stringToCollect(cookieValue);
        String cookieToken = (String) cookieValueMap.get("token");
        // 根据token获取redis中用户信息
        UserInfo userInfo = (UserInfo) redisUtil.get(cookieToken);
        if (null == userInfo){
            logger.info("redis中无用户信息");
            // 跳转到登录
            response.sendRedirect(webProperConfig.getLoginLocation());
//            request.getRequestDispatcher("/").forward(request, response);
            return false;
        }
        // 更新token失效时间
        redisUtil.expire(cookieToken,7*24*60*60);
        return true;
    }

    /*
     * 处理请求完成后视图渲染之前的处理操作
     * 通过ModelAndView参数改变显示的视图，或发往视图的方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.println("执行了postHandle方法");
    }

    /*
     * 视图渲染之后的操作
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
        System.out.println("执行到了afterCompletion方法");
    }

}
