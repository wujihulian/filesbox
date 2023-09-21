package com.svnlan.user.dao;

import com.svnlan.user.domain.GroupMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 16:56
 */
public interface GroupMetaDao {

    int delMetaByGroupID(@Param("groupID") Long groupID, @Param("list")List<String> list);

    int batchInsert(List<GroupMeta> list);

    List<GroupMeta> getGroupMetaList(@Param("groupIdList") List<Long> groupIdList, @Param("list")List<String> list);

    Long getSystemGroup();
}
