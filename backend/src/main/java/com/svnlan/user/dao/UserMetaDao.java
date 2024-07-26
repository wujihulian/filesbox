package com.svnlan.user.dao;

import com.svnlan.user.domain.UserMeta;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:07
 */
public interface UserMetaDao {

    int delMetaByUserID(Long userID, List<String> list);

    int batchInsert(List<UserMeta> list);
}
