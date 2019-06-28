package com.organization.userapi;


import com.organization.entity.UserInfo;
import com.organization.pojo.OpResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserInfoService {
	/**
	 * 用户登录
	 * @param nickname
	 * @param password
	 * @return
	 */
	OpResult userLogin(String nickname, String password);//,HttpServletResponse res

//	List<Map<String, Object>> getUserVisitLog();

	/**
	 * 用户注册
	 * @param userInfo
	 * @return
	 */
	OpResult userRegister (UserInfo userInfo) ;

	/**
	 * 修改用户信息
	 * @param userInfo
	 * @return
	 */
	OpResult updateUserInfo (UserInfo userInfo,String token) ;

	/**
	 * 用户登出
	 * @param token
	 * @return
	 */
	OpResult signOut(String token);
}
