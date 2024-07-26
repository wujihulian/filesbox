package com.svnlan.user.dao;

import com.svnlan.user.domain.GroupSource;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/27 17:06
 */
public interface GroupSourceDao {

    int insert(GroupSource groupSource);
    List<GroupSource> getGroupSourceList(List<Long> list);
    Long checkIsGroup(List<Long> list);
    List<GroupSource> getGroupSourceIDs(Long sourceID);
}
