package com.organization.dynamicapi;

import com.organization.entity.DynamicInfo;
import com.organization.pojo.OpResult;

import javax.servlet.http.HttpServletRequest;

public interface DynamicInfoService {
	/**
	 * 发布状态
	 * @param dynamicInfo
	 * @return
	 */
	OpResult addDynamicInfo(DynamicInfo dynamicInfo) ;

	/**
	 * 查询首页动态发布列表
	 * @param page
	 * @param pageSize
	 * @return
	 */
	OpResult getDynamicListOnIndex(Integer page,Integer pageSize);

	/**
	 *根据ID修改动态信息状态
	 * @param dynamicInfo
	 * @return
	 */
    OpResult updateDynamicById(DynamicInfo dynamicInfo);

	/**
	 * 后台查询动态发布列表
	 * @param page
	 * @param pageSize
	 * @return
	 */
    OpResult getDynamicListOnManage(Integer page, Integer pageSize);

	/**
	 * 查询单个动态信息及评论信息
	 * @param dynamicId
	 * @return
	 */
	OpResult getDynamicAndCommentById(String dynamicId);

	/**
	 * 获取个人动态页信息
	 * @param userId
	 * @return
	 */
	OpResult getDynamicAndCommentByUserId(String userId);
}
