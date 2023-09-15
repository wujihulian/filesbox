package com.svnlan.user.dao;

import com.svnlan.user.vo.UserGroupVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:06
 */
public interface UserGroupDao {

    List<UserGroupVo> getUserGroupInfoList(List<Long> list);
    List<UserGroupVo> getUserGroupInfoListByParam(@Param("list") List<Long> list, @Param("keyword") String keyword);
    List<UserGroupVo> getGroupNameListByUserIds(List<Long> list);
    int delByUserID(Long userID);
    int delByGroupID(Long groupID);
    int batchInsert(List<UserGroupVo> list);
    List<Long> getMyGroupIDList(Long userID);
}
