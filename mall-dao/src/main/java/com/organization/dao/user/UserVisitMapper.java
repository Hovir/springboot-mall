package com.organization.dao.user;


import com.organization.entity.UserVisit;

import java.util.List;
import java.util.Map;

public interface UserVisitMapper {

    int insert(UserVisit record);

    int insertSelective(UserVisit record);

    UserVisit selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserVisit record);

    int updateByPrimaryKey(UserVisit record);

    /**
     * 获取全部用户最新回访记录
     */
    List<Map<String, Object>> getUserVisitLog();
}