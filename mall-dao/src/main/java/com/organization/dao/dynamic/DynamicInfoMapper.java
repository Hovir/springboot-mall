package com.organization.dao.dynamic;

import com.organization.entity.DynamicInfo;

import java.util.List;
import java.util.Map;

public interface DynamicInfoMapper {

    int insertSelective(DynamicInfo record);

    DynamicInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DynamicInfo record);

    int updateByPrimaryKey(DynamicInfo record);

    /**
     * 首页查询动态发布列表
     * @return
     */
    List<DynamicInfo> getDynamicListOnIndex();

    /**
     * 后台查询动态发布列表
     * @return
     */
    List<DynamicInfo> getDynamicListOnManage();

    /**
     * 获取用户发布的所有动态列表
     * @param userId
     * @return
     */
    List<DynamicInfo> getDynamicListByUserId(String userId);
}