package com.organization.dao.dynamic;

import com.organization.entity.CommentInfo;

import java.util.List;
import java.util.Map;

public interface CommentInfoMapper {
    /**
     * 添加评论
     * @param record
     * @return
     */
    int insertSelective(CommentInfo record);

    CommentInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CommentInfo record);

    int updateByPrimaryKey(CommentInfo record);

    /**
     * 根据关联发布的动态ID查询评论列表
     * @param dynamicId
     * @return
     */
    List<CommentInfo> getCommentListByDyId(String dynamicId);

    /**
     * 根据关联发布的动态ID集合 查询评论列表
     * @param list
     * @return
     */
    List<CommentInfo> getCommentListByDyIdList(List<String> list);
}