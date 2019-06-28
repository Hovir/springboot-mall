package com.organization.dynamicapi;

import com.organization.entity.CommentInfo;
import com.organization.pojo.OpResult;

public interface CommentInfoService {
	/**
	 * 添加评论
	 * @param commentInfo
	 * @return
	 */
	OpResult addCommentInfo(CommentInfo commentInfo) ;

}
