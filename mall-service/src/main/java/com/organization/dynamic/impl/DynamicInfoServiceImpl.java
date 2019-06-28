package com.organization.dynamic.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.organization.dao.dynamic.CommentInfoMapper;
import com.organization.dao.dynamic.DynamicInfoMapper;
import com.organization.dynamicapi.DynamicInfoService;
import com.organization.entity.CommentInfo;
import com.organization.entity.DynamicInfo;
import com.organization.pojo.OpResult;
import com.organization.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service(version = "1.0.0", timeout = 10000, interfaceClass = DynamicInfoService.class)
@Transactional(rollbackFor = Exception.class)
class DynamicInfoServiceImpl implements DynamicInfoService {
    private static Logger logger = LoggerFactory.getLogger(DynamicInfoServiceImpl.class);

    @Autowired
    private DynamicInfoMapper dynamicInfoMapper;

    @Autowired
    private CommentInfoMapper commentInfoMapper;


    @Override
    public OpResult addDynamicInfo(DynamicInfo dynamicInfo) {
        OpResult op = new OpResult();
        try {

            String picPath = dynamicInfo.getPicPath();
            if (null == picPath || picPath.trim().equals("")) {
                op.setMessage("请上传图片");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            String userId = dynamicInfo.getUserId();
            String userNickname= dynamicInfo.getUserNickname();
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

            //*********生成 用户发布的动态 *****/
            String id = StringUtil.getUUID();
            dynamicInfo.setReviewStatus(0);
            dynamicInfo.setIsDisplay(1);
            dynamicInfo.setIsDelelte(0);
            dynamicInfo.setCreateTime(new Date());
            dynamicInfo.setUpdateTime(new Date());
            dynamicInfo.setId(id);
            dynamicInfo.setSort(1);
            dynamicInfo.setVersion("1.0.0");
            //**********生成 用户发布的动态 *****/

            int i = dynamicInfoMapper.insertSelective(dynamicInfo);
            if (i < 0) {
                op.setMessage("发布失败");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            //向管理端发送通知

            op.setMessage("发布成功");
            op.setStatus(OpResult.OP_SUCCESS);
            op.setDataValue("");
            return op;
        } catch (Exception e) {
            //如果有异常，事务回滚（try catch方式）
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("发布失败！e:" + e.getMessage());
            e.printStackTrace();
            op.setMessage("发布失败");
            op.setStatus(OpResult.OP_FAILED);
            op.setDataValue(null);
            return op;
        }
    }

    @Override
    public OpResult getDynamicListOnIndex(Integer page, Integer pageSize) {
        OpResult op = new OpResult();
        try {
            PageHelper.startPage(page,pageSize);
            List<DynamicInfo> dynamicInfos = dynamicInfoMapper.getDynamicListOnIndex();
            PageInfo<DynamicInfo> pageInfo = new PageInfo<>(dynamicInfos);
            op.setMessage("查询成功");
            op.setStatus(OpResult.OP_SUCCESS);
            op.setDataValue(pageInfo);
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("查询getDynamicListOnIndex失败！e:" + e.getMessage());
            e.printStackTrace();
            op.setMessage("查询失败");
            op.setStatus(OpResult.OP_FAILED);
            op.setDataValue(null);
            return op;
        }
    }

    @Override
    public OpResult updateDynamicById(DynamicInfo dynamicInfo) {
        OpResult op = new OpResult();
        try {
            String id = dynamicInfo.getId();
            if (null == id){
                op.setMessage("请传入主键ID");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            Integer isDelelte = dynamicInfo.getIsDelelte();
            Integer isDisplay = dynamicInfo.getIsDisplay();
            if (null == isDelelte && null == isDisplay) {
                op.setMessage("请传入参数");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            dynamicInfo.setUpdateTime(new Date());
            int i = dynamicInfoMapper.updateByPrimaryKeySelective(dynamicInfo);
            if (i < 0){
                op.setMessage("修改动态信息状态失败");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            op.setMessage("根据ID修改动态信息状态成功");
            op.setStatus(OpResult.OP_SUCCESS);
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("根据ID修改动态信息状态失败！e:" + e.getMessage());
            e.printStackTrace();
            op.setMessage("根据ID修改动态信息状态失败");
            op.setStatus(OpResult.OP_FAILED);
            return op;
        }
    }

    @Override
    public OpResult getDynamicListOnManage(Integer page, Integer pageSize) {
        OpResult op = new OpResult();
        try {
            PageHelper.startPage(page,pageSize);
            List<DynamicInfo> dynamicInfos = dynamicInfoMapper.getDynamicListOnManage();
            PageInfo<DynamicInfo> pageInfo = new PageInfo<>(dynamicInfos);
            op.setMessage("查询成功");
            op.setStatus(OpResult.OP_SUCCESS);
            op.setDataValue(pageInfo);
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("查询getDynamicListOnManage失败！e:" + e.getMessage());
            e.printStackTrace();
            op.setMessage("查询失败");
            op.setStatus(OpResult.OP_FAILED);
            op.setDataValue(null);
            return op;
        }
    }

    @Override
    public OpResult getDynamicAndCommentById(String dynamicId) {
        OpResult op = new OpResult();
        try {
            if (null == dynamicId || dynamicId.trim().equals("")){
                op.setMessage("请传入动态ID");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            // 获取用户动态
            DynamicInfo dynamicInfo = dynamicInfoMapper.selectByPrimaryKey(dynamicId);
            // 获取动态评论列表
            List<CommentInfo> commentListByDyId = commentInfoMapper.getCommentListByDyId(dynamicId);

            Map<String,Object> map = new HashMap<>();
            map.put("dynamicInfo",dynamicInfo);
            map.put("commentInfoList",commentListByDyId);

            // 返回结果集 使MyRelease.vue组件复用
//            List<Map<String,Object>> resultList = new ArrayList<>();
//            resultList.add(map);

            op.setDataValue(map);
            op.setStatus(OpResult.OP_SUCCESS);
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("查询单个动态信息(ID:"+dynamicId+")及评论信息失败！e:" + e.getMessage());
            e.printStackTrace();
            op.setMessage("获取失败");
            op.setStatus(OpResult.OP_FAILED);
            op.setDataValue(null);
            return op;
        }
    }

    @Override
    public OpResult getDynamicAndCommentByUserId(String userId) {
        OpResult op = new OpResult();
        try {
            if (null == userId || userId.trim().equals("")){
                op.setMessage("请传入用户ID");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            // 返回结果集
            List<Map<String,Object>> resultList = new ArrayList<>();

            // 获取用户动态
            List<DynamicInfo> dynamicListByUserId = dynamicInfoMapper.getDynamicListByUserId(userId);


            if (null == dynamicListByUserId || dynamicListByUserId.size() == 0){
                op.setMessage("用户无动态");
                op.setStatus(OpResult.OP_SUCCESS);
                op.setDataValue(resultList);
                return op;
            }

            // 用户动态ID的List
            List<String> dynamicIdList = new ArrayList<>();

            for (DynamicInfo dynamicInfo : dynamicListByUserId) {
                dynamicIdList.add(dynamicInfo.getId());
            }

            // 获取动态评论列表
            List<CommentInfo> commentListByDyIdList = commentInfoMapper.getCommentListByDyIdList(dynamicIdList);

            for (DynamicInfo dynamicInfo : dynamicListByUserId){
                // 每个动态的评论列表
                List<CommentInfo> commentInfoList = new ArrayList<>();
                if (null != commentListByDyIdList && commentListByDyIdList.size() != 0){
                    for (CommentInfo commentInfo : commentListByDyIdList) {
                        if (commentInfo.getDynamicId().equals(dynamicInfo.getId())){
                            commentInfoList.add(commentInfo);
                        }
                    }
                }
                Map<String,Object> map = new HashMap<>();
                map.put("dynamicInfo",dynamicInfo);
                map.put("commentInfoList",commentInfoList);
                resultList.add(map);
            }

            op.setDataValue(resultList);
            op.setStatus(OpResult.OP_SUCCESS);
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("查询个人动态信息及评论信息列表失败！e:" + e.getMessage());
            e.printStackTrace();
            op.setMessage("获取失败");
            op.setStatus(OpResult.OP_FAILED);
            return op;
        }
    }

}
