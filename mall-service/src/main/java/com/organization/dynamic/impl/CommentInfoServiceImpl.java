package com.organization.dynamic.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.organization.dao.dynamic.CommentInfoMapper;
import com.organization.dynamicapi.CommentInfoService;
import com.organization.entity.CommentInfo;
import com.organization.pojo.OpResult;
import com.organization.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

@Service(version = "1.0.0", timeout = 10000, interfaceClass = CommentInfoService.class)
@Transactional(rollbackFor = Exception.class)
class CommentInfoServiceImpl implements CommentInfoService {
    private static Logger logger = LoggerFactory.getLogger(CommentInfoServiceImpl.class);

    @Autowired
    private CommentInfoMapper commentInfoMapper;

    @Override
    public OpResult addCommentInfo(CommentInfo commentInfo) {
        OpResult op = new OpResult();
        try {
            
            String userId = commentInfo.getUserId();
            String userNickname = commentInfo.getUserNickname();
            String dynamicId = commentInfo.getDynamicId();
            String content = commentInfo.getContent();

            if (null == userId || userId.trim().equals("")) {
                op.setMessage("请传入用户ID");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            if (null == userNickname || userNickname.trim().equals("")) {
                op.setMessage("请传入用户昵称");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            if (null == dynamicId || dynamicId.trim().equals("")) {
                op.setMessage("请传入动态ID");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            if (null == content || content.trim().equals("")) {
                op.setMessage("请传入用户评论内容");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            //*********初始化 评论信息 *****/
            String id = StringUtil.getUUID();
            commentInfo.setIsDisplay(1);
            commentInfo.setIsDelelte(0);
            commentInfo.setCreateTime(new Date());
            commentInfo.setUpdateTime(new Date());
            commentInfo.setId(id);
            commentInfo.setSort(1);
            commentInfo.setVersion("1.0.0");
            //**********初始化 评论信息 *****/

            int i = commentInfoMapper.insertSelective(commentInfo);
            if (i < 0) {
                op.setMessage("添加评论失败");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            op.setMessage("评论成功");
            op.setStatus(OpResult.OP_SUCCESS);
            op.setDataValue("");
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("评论失败！e:" + e.getMessage());
            e.printStackTrace();
            op.setMessage("评论失败");
            op.setStatus(OpResult.OP_FAILED);
            return op;
        }
    }

}
