package com.organization.controller.dynamic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.organization.dynamicapi.DynamicInfoService;
import com.organization.entity.DynamicInfo;
import com.organization.pojo.OpResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther:zhw
 * @Description 用户发布的动态
 * @Date:Created in 16:50 2019/6/1
 **/
@Controller
@RequestMapping("/dynamic")
public class DynamicController {

    @Reference(version = "1.0.0")
    private DynamicInfoService dynamicInfoService;

    /**
     * 用户发布动态
     * @param dynamicInfo
     * @return
     */
    @RequestMapping("/addDynamicInfo")
    @ResponseBody
    public OpResult addDynamicInfo(DynamicInfo dynamicInfo){
        return dynamicInfoService.addDynamicInfo(dynamicInfo);
    }

    /**
     * 首页查询动态发布列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getDynamicListOnIndex")
    @ResponseBody
    public OpResult getDynamicListOnIndex(
         @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
         @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,HttpServletRequest request){
        System.out.println("首页传入请求地址："+request.getRequestURL());
        return dynamicInfoService.getDynamicListOnIndex(page,pageSize);
    }

    /**
     * 后台查询动态发布列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getDynamicListOnManage")
    @ResponseBody
    public OpResult getDynamicListOnManage(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize){
        return dynamicInfoService.getDynamicListOnManage(page,pageSize);
    }


    /**
     * 根据ID修改动态信息状态
     * @param dynamicInfo
     * @return
     */
    @RequestMapping("/updateDynamicById")
    @ResponseBody
    public OpResult updateDynamicById(DynamicInfo dynamicInfo){
        return dynamicInfoService.updateDynamicById(dynamicInfo);
    }

    /**
     * 获取用户个人动态信息
     * @param userId
     * @return
     */
    @RequestMapping("/getDynamicAndCommentByUserId")
    @ResponseBody
    public OpResult getDynamicAndCommentByUserId(String userId){
        return dynamicInfoService.getDynamicAndCommentByUserId(userId);
    }

    /**
     * 获取单个动态信息及评论列表
     * @param dynamicId
     * @return
     */
    @RequestMapping("/getDynamicAndCommentById")
    @ResponseBody
    public OpResult getDynamicAndCommentById(String dynamicId){
        return dynamicInfoService.getDynamicAndCommentById(dynamicId);
    }
}
