package com.organization.dao.user;

import com.organization.entity.UserInfo;

import java.util.Map;

public interface UserInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo userLogin(Map<String, Object> map);

    int checkNickname(String nickname);
}