package com.organization.user.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.organization.dao.user.UserVisitMapper;
import com.organization.pojo.OpResult;
import com.organization.userapi.UserInfoService;
import com.organization.dao.user.UserInfoMapper;
import com.organization.entity.UserInfo;
//import org.springframework.stereotype.Service;
import com.organization.utils.CookieUtil;
import com.organization.utils.RedisUtil;
import com.organization.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//@Component
@Service(version = "1.0.0", timeout = 10000, interfaceClass = UserInfoService.class)
@Transactional(rollbackFor = Exception.class)
public class UserInfoServiceImpl implements UserInfoService {
    private static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Resource
    private UserInfoMapper userInfoMapper;

//    @Resource
//    private UserVisitMapper userVisitMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public OpResult userLogin(String nickname, String password) {
        OpResult op = new OpResult();
        try {
            if (null == nickname || nickname.trim().equals("")) {
                logger.info("传入空用户名");
                op.setMessage("用户名不能为空");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            if (null == password || password.trim().equals("")) {
                logger.info("传入空密码");
                op.setMessage("密码不能为空");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("nickname", nickname);
            map.put("password", password);
            UserInfo userInfo = userInfoMapper.userLogin(map);
            if (null == userInfo){
                logger.info("用户名或密码错误");
                op.setMessage("用户名或密码错误");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            //生成token
            String token = StringUtil.getUUID();
            // 将用户信息放入redis中
            redisUtil.set(token, userInfo);
            // 失效时间7天
            redisUtil.expire(token,7*24*60*60);

            // 封装返回值
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("token", token);
            resultMap.put("userInfo", userInfo);

            op.setDataValue(resultMap);
            op.setStatus(OpResult.OP_SUCCESS);
            op.setMessage("登录成功");
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("登录失败,e:" + e.getMessage());
            e.printStackTrace();
            return op;
        }
    }

    @Override
    public OpResult userRegister(UserInfo userInfo) {
        OpResult op = new OpResult();
        try {
            String nickname = userInfo.getNickname();
            String password = userInfo.getPassword();
            if (null == nickname || nickname.trim().equals("")) {
                op.setMessage("昵称不能为空");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            if (null == password || password.trim().equals("")) {
                op.setMessage("密码不能为空");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            //校验昵称是否重复（真实项目中会校验用户唯一标识，例如：用户手机号）
            int count = userInfoMapper.checkNickname(nickname);
            if (count > 0){
                op.setMessage("昵称已注册");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            //生成用户信息
            String userId = StringUtil.getUUID();
            userInfo.setCreateTime(new Date());
            userInfo.setUpdateTime(new Date());
            userInfo.setId(userId);
            userInfo.setSort(1);
            userInfo.setVersion("1.0.0");
            userInfo.setVipLevel(0);
            int i = userInfoMapper.insertSelective(userInfo);
            if (i < 0) {
                op.setMessage("注册失败");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            op.setMessage("注册成功");
            op.setStatus(OpResult.OP_SUCCESS);
            op.setDataValue(userId);
            return op;
        } catch (Exception e) {
            //如果有异常，事务回滚（try catch方式）
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("用户注册失败！e:" + e.getMessage());
            e.printStackTrace();
            op.setMessage("注册失败");
            op.setStatus(OpResult.OP_FAILED);
            op.setDataValue(null);
            return op;
        }
    }

    @Override
    public OpResult updateUserInfo(UserInfo userInfo,String token) {
        OpResult op = new OpResult();
        try {
            if (null == userInfo){
                op.setMessage("请传入用户信息");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            if (null == userInfo.getId() || userInfo.getId().trim().equals("")){
                op.setMessage("请传入用户ID");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            if (null == userInfo.getNickname() || userInfo.getNickname().trim().equals("")){
                op.setMessage("请传入用户昵称");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            //修改用户信息
            userInfo.setUpdateTime(new Date());
            int i = userInfoMapper.updateByPrimaryKeySelective(userInfo);
            if (i < 0){
                op.setMessage("修改失败");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }

            // 根据用户ID查询修改后的用户信息
            UserInfo updateUserInfo = userInfoMapper.selectByPrimaryKey(userInfo.getId());
            if(null == updateUserInfo){
                logger.error("修改用户信息失败,修改后用户信息丢失");
                op.setMessage("修改失败");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            // 更新redis
            redisUtil.set(token, updateUserInfo);
            // 失效时间7天
            redisUtil.expire(token,7*24*60*60);

            // 封装返回值
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("token", token);
            resultMap.put("userInfo", updateUserInfo);

            op.setDataValue(resultMap);
            op.setStatus(OpResult.OP_SUCCESS);
            op.setMessage("修改用户信息成功");
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("修改用户信息失败,e:" + e.getMessage());
            e.printStackTrace();
            return op;
        }
    }

    @Override
    public OpResult signOut(String token) {
        OpResult op = new OpResult();
        try {
            if (null == token || token.trim().equals("")){
                op.setMessage("token不能为空");
                op.setStatus(OpResult.OP_FAILED);
                return op;
            }
            // 清除redis中数据
            redisUtil.delete(token);
            logger.info("用户登出，redis清除成功");
            op.setStatus(OpResult.OP_SUCCESS);
            op.setMessage("登出成功");
            return op;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("登出失败,e:" + e.getMessage());
            e.printStackTrace();
            return op;
        }
    }


    //    @Override
//    public List<Map<String, Object>> getUserVisitLog() {
//        List<Map<String, Object>> userVisitLog = null;
//        try {
//            userVisitLog = userVisitMapper.getUserVisitLog();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return userVisitLog;
//    }
}
