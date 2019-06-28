package com.organization.controller.dynamic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.organization.dynamicapi.CommentInfoService;
import com.organization.entity.CommentInfo;
import com.organization.pojo.OpResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comment")
public class CommentInfoController {

    @Reference(version = "1.0.0")
    private CommentInfoService commentInfoService;

    /**
     * 用户发表评论
     * @param commentInfo
     * @return
     */
    @RequestMapping("/addCommentInfo")
    @ResponseBody
    public OpResult addCommentInfo(CommentInfo commentInfo){
        return commentInfoService.addCommentInfo(commentInfo);
    }

}
