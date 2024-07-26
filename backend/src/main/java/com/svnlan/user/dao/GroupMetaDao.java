package com.svnlan.user.dao;

import com.svnlan.user.domain.GroupMeta;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 16:56
 */
public interface GroupMetaDao {

    int delMetaByGroupID(Long groupID, List<String> list);

    int batchInsert(List<GroupMeta> list);

    List<GroupMeta> getGroupMetaList(List<Long> groupIdList, List<String> list);

    Long getSystemGroup();
}
