package com.svnlan.user.dao;

import com.svnlan.user.vo.UserGroupVo;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:06
 */
public interface UserGroupDao {

    List<UserGroupVo> getUserGroupInfoList(List<Long> list);
    List<UserGroupVo> getUserGroupInfoListByParam( List<Long> list, String keyword);
    List<UserGroupVo> getGroupNameListByUserIds(List<Long> list);
    int delByUserID(Long userID);
    int delByGroupID(Long groupID);
    int batchInsert(List<UserGroupVo> list);
    List<Long> getMyGroupIDList(Long userID);
    Long checkRelationIsExsit(Long userId, Long groupId);
}
