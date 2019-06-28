package com.organization.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.organization.config.WebProperConfig;
import com.organization.pojo.OpResult;
import com.organization.userapi.UserInfoService;
import com.organization.entity.UserInfo;
import com.organization.utils.CookieUtil;
import com.organization.utils.FastJsonUtils;
import com.organization.utils.UploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Auther:zhw
 * @Description
 **/
@Controller
//@EnableAutoConfiguration
@RequestMapping("/userInfo")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Reference(version = "1.0.0")
    private UserInfoService userInfoService;

    @Autowired
    private WebProperConfig webProperConfig;
    /**
     * 用户注册
     * @param userInfo
     * @return
     */
    @RequestMapping("/userRegister")
    @ResponseBody
    public OpResult userRegister(UserInfo userInfo){
        return userInfoService.userRegister(userInfo);
    }

    /**
     * 用户登录
     * @param nickname
     * @param password
     * @return
     */
    @RequestMapping(value = "/userLogin" , method = RequestMethod.POST)
    @ResponseBody
    public OpResult userLogin(String nickname, String password, HttpServletResponse res){
        OpResult opResult = userInfoService.userLogin(nickname, password);
        if (opResult.getStatus() == 1){
            setCookieInfo(opResult,res);
        }
        return opResult;
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @RequestMapping("/signOut")
    @ResponseBody
    public OpResult signOut(HttpServletRequest request,HttpServletResponse response){//String token
        String token = getTokenFromCookie(request);
        logger.info("从cookie中获取的token:" + token);
        OpResult opResult = userInfoService.signOut(token);
        if (opResult.getStatus() == 1) {
            String cooikeDomain = webProperConfig.getCooikeDomain();
            if(cooikeDomain.equals("") || null == cooikeDomain){
                cooikeDomain = null;
            }
            if (CookieUtil.deleteCookie(request,response,"Login_token",cooikeDomain)) {
                logger.info("用户登出，cooike清除成功");
            }
        }
        return opResult;
    }

    /**
     * 修改用户信息
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/updateUserInfo" , method = RequestMethod.POST)
    @ResponseBody
    public OpResult updateUserInfo(UserInfo userInfo,HttpServletResponse res,HttpServletRequest req){
        String token = getTokenFromCookie(req);
        logger.info("从cookie中获取的token:" + token);
        OpResult opResult = userInfoService.updateUserInfo(userInfo,token);
        // 修改用户信息后更新cookie
        if (opResult.getStatus() == 1){
            setCookieInfo(opResult,res);
        }
        return opResult;
    }


    /**
     * 文件上传
     * @param file
     * @param req
     * @return
     */
    @RequestMapping(value = "/uploadFile" , method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file,String folder, HttpServletRequest req){
        return UploadUtil.uploadPhotoToOss(file, folder, req);
    }

    /**
     * 从cookie中获取token
     * @param req
     */
    private String getTokenFromCookie(HttpServletRequest req){
        // 获取cookie
        Cookie loginToken = CookieUtil.getCookie(req,"Login_token");
        // 以utf-8编码格式获取cookie中的value
        String cookieValue = CookieUtil.getValue(loginToken);
        // 将value(json字符串)解析成map
        Map<Object, Object> cookieMap = FastJsonUtils.stringToCollect(cookieValue);
        String token = (String) cookieMap.get("token");
        return token;
    }

    /**
     * 添加或更新cookie信息
     * @param opResult
     */
    private void setCookieInfo(OpResult opResult,HttpServletResponse res){
        Object dataValue = opResult.getDataValue();
        String jsonValue = FastJsonUtils.convertObjectToJSON(dataValue);
        String cooikeDomain = webProperConfig.getCooikeDomain();
        if(cooikeDomain.equals("") || null == cooikeDomain){
            cooikeDomain = null;
        }
        CookieUtil.addCookie(res,"Login_token",jsonValue,cooikeDomain,7*24*60*60);
    }



//    @RequestMapping("/getUserVisitLog")
//    @ResponseBody
//    public List<Map<String, Object>> getUserVisitLog(){
//        return userInfoService.getUserVisitLog();
//    }

}
